package com.example.todolist.entity;

import com.example.todolist.enums.Level;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "priority_gen", sequenceName = "priority_seq")
public class PriorityEntity {

	@Id
	@GeneratedValue(generator = "priority_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private Level level;
	
	@Builder
	public PriorityEntity(Long id, Level level) {
		this.level = level;
	}
	
}