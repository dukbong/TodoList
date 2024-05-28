package com.example.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todolist.entity.PriorityEntity;
import com.example.todolist.enums.Level;

public interface PriorityRepository extends JpaRepository<PriorityEntity, Long> {
	Optional<PriorityEntity> findByLevel(Level level);
}
