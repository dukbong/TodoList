package com.example.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.dto.Join;
import com.example.todolist.dto.Login;
import com.example.todolist.service.BasicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class BasicController {
	
	private final BasicService basicService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		String jwtToken = basicService.loginProcess(login);
		return ResponseEntity.ok().body("Login Successful.");
	}
	
	@PostMapping
	public ResponseEntity<String> join(@RequestBody Join join) {
		return ResponseEntity.ok().body("Join Successful.");
	}
	
	@PostMapping("/check-email/send")
	public ResponseEntity<String> checkEmailSend(@RequestBody String email) {
		basicService.sendEmail(email);
		return ResponseEntity.ok().body("Check email sent.");
	}
	
	@GetMapping("/check-email/validate")
	public ResponseEntity<String> checkEmailValidation(@RequestParam("email") String email, @RequestParam("token") String token) {
		basicService.checkEmail(email, token);
		return ResponseEntity.ok().body("Email validation successful.");
	}
}
