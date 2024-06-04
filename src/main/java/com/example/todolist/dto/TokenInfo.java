package com.example.todolist.dto;

import com.example.todolist.security.jwt.enums.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {
	
	private String username;
	private String password;
	private Role role;
	
}
