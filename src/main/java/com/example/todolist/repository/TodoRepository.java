package com.example.todolist.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.todolist.entity.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
	@Query("SELECT t FROM TodoEntity t JOIN FETCH t.userEntity u WHERE u.id = :userId AND u.useYn = 'Y' AND t.todoStatus != com.example.todolist.enums.TodoStatus.CANCELLED ORDER BY t.id ASC")
	List<TodoEntity> getTodos(@Param("userId")Long userId);
	
	@Query("SELECT t FROM TodoEntity t JOIN FETCH t.userEntity u WHERE u.id = :userId AND u.useYn = 'Y' AND t.todoStatus != com.example.todolist.enums.TodoStatus.CANCELLED AND t.startLine BETWEEN :start AND :end ORDER BY t.id ASC")
	List<TodoEntity> getBetweenTodos(@Param("userId")Long userId, @Param("start")LocalDate start, @Param("end")LocalDate end);
}
