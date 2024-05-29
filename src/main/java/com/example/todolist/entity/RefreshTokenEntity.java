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
@SequenceGenerator(name = "refresh_token_gen", sequenceName = "refresh_token_seq")
public class RefreshTokenEntity {

	@Id
	@GeneratedValue(generator = "refresh_token_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String refreshToken;
	
	@Builder
	public RefreshTokenEntity(Long id, String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}
