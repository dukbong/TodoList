package com.example.todolist.service;

import org.springframework.stereotype.Service;

import com.example.todolist.dto.Login;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicService {

	private final MailService mailService;
	
	public void sendEmail(String email) {
		mailService.sendEmail(email);
	}
	
	public void checkEmail(String email, String authenticationText) {
		// Redis에서 확인하기
	}

	public String loginProcess(Login login) {
		return null;
	}

}
