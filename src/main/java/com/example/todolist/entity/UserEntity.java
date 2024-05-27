package com.example.todolist.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "user_gen", sequenceName = "user_seq")
public class UserEntity {

	@Id
	@GeneratedValue(generator = "user_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String username;
	
	private String password;
	
	private String role;
	
	@OneToMany(mappedBy = "userEntity")
	private List<TodoEntity> todos = new ArrayList<>();

	public UserEntity(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
}