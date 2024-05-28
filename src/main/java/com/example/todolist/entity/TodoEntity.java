package com.example.todolist.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.todolist.dto.Todo;
import com.example.todolist.enums.TodoStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id")
	private PriorityEntity priorityEntity;
	
	private String content;
	
	private LocalDate startLine;
	
	private LocalDate deadLine;
	
	@Enumerated(EnumType.STRING)
	private TodoStatus todoStatus;
	
	private LocalDateTime createAt;
	
	@Builder
	public TodoEntity(UserEntity userEntity, PriorityEntity priorityEntity, String content, LocalDate startLine, LocalDate deadLine,
			LocalDateTime createAt, TodoStatus todoStatus) {
		this.userEntity = userEntity;
		this.priorityEntity = priorityEntity;
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
		this.todoStatus = TodoStatus.COMPLETED;
		relationShipConnection();
	}
	
	public void cancel() {
		this.todoStatus = TodoStatus.CANCELLED;
		relationShipConnection();
	}

	public void changePriority(PriorityEntity priorityEntity) {
		this.priorityEntity = priorityEntity;
		relationShipConnection();
	}
	
	private void relationShipConnection() {
		this.userEntity.getTodos().removeIf(todo -> todo.getId().equals(this.id));
		this.userEntity.getTodos().add(this);
	}
	
	public Todo toDTO() {
		return Todo.builder().creator(this.getUserEntity().getUsername())
							 .content(this.content)
							 .startLine(this.startLine)
							 .deadLine(this.deadLine)
							 .createAt(this.createAt)
							 .priority(this.priorityEntity.getLevel())
							 .todoStatus(this.todoStatus)
							 .build();
	}
	
}
