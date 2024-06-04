package com.example.todolist.security.jwt.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.todolist.dto.TokenInfo;
import com.example.todolist.entity.UserEntity;
import com.example.todolist.repository.UserRepository;
import com.example.todolist.security.jwt.details.CustomUserDetails;
import com.example.todolist.security.jwt.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

	@Override
	public UserDetails loadUserByUsernamePassword(String username, String password) {
		UserEntity userEntity = userRepository.findByUsernameAndPasswordAndUseYn(username, password, "Y").orElseThrow(() -> new IllegalArgumentException("Not Found UserEntity."));
		return new CustomUserDetails(TokenInfo.builder().username(userEntity.getUsername()).password(userEntity.getPassword()).role(userEntity.getRole()).build());
	}

}
