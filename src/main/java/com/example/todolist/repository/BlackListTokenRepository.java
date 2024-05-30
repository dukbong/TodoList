package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todolist.entity.BlackListTokenEntity;

public interface BlackListTokenRepository extends JpaRepository<BlackListTokenEntity, Long> {
}
