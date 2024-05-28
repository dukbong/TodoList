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
import com.example.todolist.enums.Level;
import com.example.todolist.service.TodoService;

import lombok.RequiredArgsConstructor;

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
		// 임시로 진행, 추후에는 모두 Authenticaion 객체에서 가져오게 만들기.
		return ResponseEntity.ok().body(todoService.getTodos(1L));
	}
	
	@GetMapping("/{startLine}/{deadLine}")
	public ResponseEntity<List<Todo>> getBetweenTodos(@PathVariable("startLine") String StartLine, @PathVariable("deadLine") String deadLine) {
		return ResponseEntity.ok().body(null);
	}
	
	@PatchMapping("/{todoId}/priority/{priorityLevel}")
	public ResponseEntity<String> changeTodoPriority(@PathVariable("todoId") Long todoId, @PathVariable("priorityLevel") Level level) {
		todoService.changeTodoPriority(todoId, level);
		return ResponseEntity.ok().body("Todo Priority has been update.");
	}
	
}
