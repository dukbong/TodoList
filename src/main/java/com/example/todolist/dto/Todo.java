package com.example.todolist.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Todo {

	private String creator;
	
	private String content;

	private LocalDate startLine;
	
	private LocalDate deadLine;
	
	private LocalDateTime createAt;
	
	@Builder
	public Todo(String creator, String content, LocalDateTime createAt, LocalDate deadLine, LocalDate startLine) {
		this.creator = creator;
		this.content = content;
		this.createAt = createAt;
		this.startLine = startLine;
		this.deadLine = deadLine;
	}
	
}
