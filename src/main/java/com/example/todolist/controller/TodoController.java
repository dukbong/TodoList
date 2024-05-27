package com.example.todolist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.dto.Todo;
import com.example.todolist.service.TodoService;

import lombok.RequiredArgsConstructor;

/***
 * 기능 1. 할일 추가 (insert)
 * 기능 2. 할일 성공 표시 (update)
 * 기능 3. 할일 취소 표시 (update)
 * 기능 4. 할일 모두 표시 (select)
 * 기능 5. 할일 시작 날짜로 조회 (select)
 * @return
 */
@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
	
	private final TodoService todoService;
	
	@PostMapping
	public ResponseEntity<String> createTodo(@RequestBody Todo todo) {
		todoService.createTodo(todo);
		return ResponseEntity.ok().body("Todos have been added.");
	}
	
	@PatchMapping("/{todoId}/complete")
	public ResponseEntity<String> completeTodo(@PathVariable("todoId") Long todoId) {
		todoService.completeTodo(todoId);
		return ResponseEntity.ok().body("The task has been completed.");
	}
	
	@DeleteMapping("/{todoId}/cancel")
	public ResponseEntity<String> cancelTodo(@PathVariable("todoId") Long todoId) {
		todoService.cancelTodo(todoId);
		return ResponseEntity.ok().body("To-do has been cancelled.");
	}
	
	@GetMapping
	public ResponseEntity<List<Todo>> getTodos() {
		todoService.getTodos();
		return ResponseEntity.ok().body(null);
	}
	
	@GetMapping("/{startLine}/{deadLine}")
	public ResponseEntity<List<Todo>> getBetweenTodos(@PathVariable("startLine") String StartLine, @PathVariable("deadLine") String deadLine) {
		return ResponseEntity.ok().body(null);
	}
	
}
