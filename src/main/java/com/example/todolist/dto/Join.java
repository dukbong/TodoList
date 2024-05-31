package com.example.todolist.dto;

import com.example.todolist.entity.UserEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Join {

	private String username;
	private String password;
	private String email;
	
	@Builder
	public Join(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	public UserEntity convertUserEntity() {
		return UserEntity.builder().username(username).password(password).role("ROLE_USER").build();
	}
}
