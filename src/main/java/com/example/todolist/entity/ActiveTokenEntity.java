package com.example.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SequenceGenerator(name = "active_token_gen", sequenceName = "active_token_seq")
public class ActiveTokenEntity {

	@Id
	@GeneratedValue(generator = "active_token_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String activeToken;
	
	@Builder
	public ActiveTokenEntity(Long id, String activeToken) {
		this.activeToken = activeToken;
	}
	
}
