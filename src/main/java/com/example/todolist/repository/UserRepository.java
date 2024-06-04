package com.example.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todolist.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsernameAndUseYn(String username, String useYn);

	Optional<UserEntity> findByUsernameAndPasswordAndUseYn(String username, String password, String useYn);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
