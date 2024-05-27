package com.example.todolist.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todolist.dto.Todo;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.entity.UserEntity;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public void createTodo(Todo todo) {
		UserEntity findUserEntity = userRepository.findByUserName(todo.getCreator()).orElseThrow(() -> new IllegalArgumentException("Not Found UserEntity."));
		TodoEntity todoEntity = TodoEntity.builder().userEntity(findUserEntity)
													.content(todo.getContent())
													.startLine(todo.getStartLine())
													.deadLine(todo.getDeadLine())
													.createAt(todo.getCreateAt())
													.todoStatus(0)
													.build();
		todoRepository.save(todoEntity);
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

}
