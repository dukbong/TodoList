package com.example.todolist.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.todolist.dto.Todo;
import com.example.todolist.entity.PriorityEntity;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.entity.UserEntity;
import com.example.todolist.enums.Level;
import com.example.todolist.enums.TodoStatus;
import com.example.todolist.repository.PriorityRepository;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	private final PriorityRepository priorityRepository;
	
	public void createTodo(Todo todo) {
		// 사용자 조회
		UserEntity findUserEntity = userRepository.findByUsernameAndUseYn(todo.getCreator(), "Y").orElseThrow(() -> new IllegalArgumentException("Not Found UserEntity."));
		
		// 우선 순위 조회
		PriorityEntity MidPriority = priorityRepository.findByLevel(Level.MID).orElseThrow(() -> new IllegalArgumentException("MID priority Not Found."));
		
		// Todo 생성
		TodoEntity todoEntity = TodoEntity.builder().userEntity(findUserEntity)
													.priorityEntity(MidPriority)
													.content(todo.getContent())
													.startLine(todo.getStartLine())
													.deadLine(todo.getDeadLine())
													.createAt(todo.getCreateAt())
													.todoStatus(TodoStatus.PENDING)
													.build();
		// Insert
		todoRepository.save(todoEntity);
		
		// 양방향 관계를 위한 편의 메소드
		// 외래키를 가진 객체에서 진행해야한다.
		todoEntity.addTodo();
	}

	public void completeTodo(Long todoId) {
		TodoEntity findTodoEntity = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("Not Found TodoEntity."));
		findTodoEntity.complete();
	}
	
	public void cancelTodo(Long todoId) {
		TodoEntity findTodoEntity = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("Not Found TodoEntity."));
		findTodoEntity.cancel();
	}

//	public List<Todo> getTodos2(Long userId) {
//		UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Not Found UserEntity."));
//		// 내림차순
//		// Comparator<TodoEntity> idComparator = Comparator.comparing(TodoEntity::getId).reversed();
//		// 오름차순
//		Comparator<TodoEntity> idComparator = Comparator.comparing(TodoEntity::getId);
//		return userEntity.getTodos().stream()
//					                .sorted(idComparator)
//					                .map(TodoEntity::toDTO)  // TodoEntity를 TodoDTO로 변환
//					                .collect(Collectors.toList());
//	}
	
	// Fetch Join
    public List<Todo> getTodos(Long userId) {
        List<TodoEntity> todos = todoRepository.getTodos(userId);
        return todos.stream()
                .map(TodoEntity::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<Todo> getBetweenTodos(Long userId, LocalDate start, LocalDate end) {
    	Assert.isTrue(start.isBefore(end),"The start date cannot be greater than the end date.");
    	List<TodoEntity> todos = todoRepository.getBetweenTodos(userId, start, end);
    	return todos.stream()
    				.map(TodoEntity::toDTO)
    				.collect(Collectors.toList());
    }

	public void changeTodoPriority(Long todoId, Level level) {
		// 우선 순위 조회
		PriorityEntity findPriority = priorityRepository.findByLevel(level).orElseThrow(() -> new IllegalArgumentException("Not Found Priority Level."));
		
		// Todo 조회
		TodoEntity findTodoEntity = todoRepository.findById(todoId).orElseThrow(() -> new IllegalArgumentException("Not Found TodoEntity."));
		
		// Todo 객체에서 변경 (업데이트)
		findTodoEntity.changePriority(findPriority);
	}

}
