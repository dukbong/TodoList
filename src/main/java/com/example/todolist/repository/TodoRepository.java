package com.example.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.todolist.entity.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
	@Query("SELECT t FROM TodoEntity t JOIN FETCH t.userEntity u WHERE u.id = :userId ORDER BY t.id ASC")
	List<TodoEntity> getTodosWithUserFetchJoin(Long userId);
}
