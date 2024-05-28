package com.example.todolist.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.todolist.enums.Level;
import com.example.todolist.enums.TodoStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Todo {

	private Long id;
	
	private String creator;
	
	private String content;

	private LocalDate startLine;
	
	private LocalDate deadLine;
	
	private Level priority;
	
	private TodoStatus todoStatus;
	
	private LocalDateTime createAt;
	
	@Builder
	public Todo(Long id, String creator, String content, LocalDateTime createAt, LocalDate deadLine, LocalDate startLine, Level priority, TodoStatus todoStatus) {
		this.id = id;
		this.creator = creator;
		this.content = content;
		this.createAt = createAt;
		this.startLine = startLine;
		this.deadLine = deadLine;
		this.priority = priority;
		this.todoStatus = todoStatus;
	}
	
}
