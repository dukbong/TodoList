package com.example.todolist.security.jwt.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {

	public UserDetails loadUserByUsernamePassword(String username, String password);
	
}
