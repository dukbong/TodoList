package com.example.todolist.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.example.todolist.dto.Join;
import com.example.todolist.dto.Login;
import com.example.todolist.enums.PasswordRegex;
import com.example.todolist.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicService {

	private final UserRepository userRepository;
	private final MailService mailService;
	
	public void checkEmailSend(String email) {
		mailService.sendEmail(emailValidation(email));
	}
	
	public void checkEmailValidation(String email, String authenticationText) {
		// Redis에서 확인하기 : 제한 시간 1분
	}

	public String loginProcess(Login login) {
		return null;
	}

	public void joinProcess(Join join) {
		passwordValidation(join.getPassword()); 
		usernameValidation(join.getUsername());
		userRepository.save(join.convertUserEntity());
	}
	
	private String emailValidation(String email) {
		boolean emailCheck = userRepository.existsByEmail(email);
		
		if(emailCheck) {
			throw new DuplicateKeyException("This email already exists.");
		}
		
		return email;
	}
	
	private void usernameValidation(String username) {
		boolean usernameCheck = userRepository.existsByUsername(username);
		
		if(usernameCheck) {
			throw new DuplicateKeyException("This username already exists.");
		}
	}
	
	private void passwordValidation(String password) {
		if (password == null || password.isEmpty() || password.length() < 8) {
			throw new IllegalArgumentException("Password must be at least 8 characters.");
		}

		if (!password.matches(PasswordRegex.UPPERCASE.getRegex())) {
			throw new IllegalArgumentException("The password must contain at least one uppercase letter.");
		}

		if (!password.matches(PasswordRegex.LOWERCASE.getRegex())) {
			throw new IllegalArgumentException("The password must contain at least one lowercase letter.");
		}

		if (!password.matches(PasswordRegex.SPECIAL_CHARACTER.getRegex())) {
			throw new IllegalArgumentException("The password must contain at least one special character.");
		}
	}

}
