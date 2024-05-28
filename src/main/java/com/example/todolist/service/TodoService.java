package com.example.todolist.service;

import org.springframework.stereotype.Service;

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
		UserEntity findUserEntity = userRepository.findByUserName(todo.getCreator()).orElseThrow(() -> new IllegalArgumentException("Not Found UserEntity."));
		
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

	public void getTodos() {
		// TODO Auto-generated method stub
		
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
