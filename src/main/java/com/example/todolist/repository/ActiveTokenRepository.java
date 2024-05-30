package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todolist.entity.ActiveTokenEntity;

public interface ActiveTokenRepository extends JpaRepository<ActiveTokenEntity, Long> {
}
