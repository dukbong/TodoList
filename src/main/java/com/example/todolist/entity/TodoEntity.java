package com.example.todolist.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Check;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "todo_gen", sequenceName = "todo_seq")
public class TodoEntity {

	@Id
	@GeneratedValue(generator = "todo_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_entity_id")
	private UserEntity userEntity;
	
	private String content;
	
	private LocalDate startLine;
	
	private LocalDate deadLine;
	
	@Check(constraints = "todoStatus IN (0, 1, 2)")
	private int todoStatus;
	
	private LocalDateTime createAt;
	
	@Builder
	public TodoEntity(UserEntity userEntity, String content, LocalDate startLine, LocalDate deadLine,
			LocalDateTime createAt, int todoStatus) {
		this.userEntity = userEntity;
		this.content = content;
		this.startLine = startLine;
		this.deadLine = deadLine;
		this.createAt = createAt;
		this.todoStatus = todoStatus;
	}
	
	public void addTodo() {
		this.userEntity.getTodos().add(this);
	}
	
	public void complete() {
		this.todoStatus = 1;
	}
	
	public void cancel() {
		this.todoStatus = 2;
	}
	
}
