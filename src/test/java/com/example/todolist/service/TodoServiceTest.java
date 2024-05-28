package com.example.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.todolist.dto.Todo;
import com.example.todolist.entity.PriorityEntity;
import com.example.todolist.entity.TodoEntity;
import com.example.todolist.entity.UserEntity;
import com.example.todolist.enums.Level;
import com.example.todolist.enums.TodoStatus;
import com.example.todolist.repository.PriorityRepository;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
	@Mock
	private TodoRepository todoRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private PriorityRepository priorityRepository;
	
	@InjectMocks
	private TodoService todoService;
	
	public static Todo createTodo(String creator, String content, LocalDateTime createAt, LocalDate startLine, LocalDate deadLine) {
		return Todo.builder().creator(creator).content(content).createAt(createAt).startLine(startLine).deadLine(deadLine).build();
	}
	
	public static UserEntity createUserEntity(String username, String password, String role) {
		return UserEntity.builder().username(username).password(password).role(role).build();
	}
	
	public static PriorityEntity craetePriorityEntity(Level level) {
		return PriorityEntity.builder().level(level).build();
	}
	
	public static TodoEntity createTodoEntity(UserEntity userEntity, PriorityEntity priorityEntity, String content, LocalDate startLine, LocalDate deadLine, LocalDateTime createAt) {
		return TodoEntity.builder().userEntity(userEntity).priorityEntity(priorityEntity).content(content).startLine(startLine).deadLine(deadLine).createAt(createAt).todoStatus(TodoStatus.PENDING).build();
	}
	
	@Test
	void createTodoWhenUserNotFound() {
		// given
		Todo todo = createTodo("dukbong", "todoList", LocalDateTime.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
		Mockito.when(userRepository.findByUsername(todo.getCreator())).thenReturn(Optional.empty());
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.createTodo(todo));
		assertThat(exception.getMessage()).isEqualTo("Not Found UserEntity.");
		
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(todo.getCreator());
	}
	
	@Test
	void createTodoWhenPriorityNotFound() {
		// given
		Todo todo = createTodo("dukbong", "todoList", LocalDateTime.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		
		Mockito.when(userRepository.findByUsername(todo.getCreator())).thenReturn(Optional.of(userEntity));
		Mockito.when(priorityRepository.findByLevel(Level.MID)).thenReturn(Optional.empty());
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.createTodo(todo));
		assertThat(exception.getMessage()).isEqualTo("MID priority Not Found.");
		
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(todo.getCreator());
		Mockito.verify(priorityRepository, Mockito.times(1)).findByLevel(Level.MID);
	}
	
	@Test
	void createTodoSuccessful() {
		// given
		Todo todo = createTodo("dukbong", "todoList", LocalDateTime.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		PriorityEntity priorityEntity = craetePriorityEntity(Level.MID);
		
		Mockito.when(userRepository.findByUsername(todo.getCreator())).thenReturn(Optional.of(userEntity));
		Mockito.when(priorityRepository.findByLevel(Level.MID)).thenReturn(Optional.of(priorityEntity));
		
		// when
		todoService.createTodo(todo);
		
		// then
		assertThat(userEntity.getTodos().size()).isEqualTo(1);
		
		Mockito.verify(userRepository, Mockito.times(1)).findByUsername(todo.getCreator());
		Mockito.verify(priorityRepository, Mockito.times(1)).findByLevel(Level.MID);
		Mockito.verify(todoRepository, Mockito.times(1)).save(Mockito.any(TodoEntity.class));
	}
	
	@Test
	void completeTodoWhenTodoNotFound() {
		// given
		Long todoId = 1L;
		Mockito.when(todoRepository.findById(todoId)).thenReturn(Optional.empty());
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.completeTodo(todoId));
		assertThat(exception.getMessage()).isEqualTo("Not Found TodoEntity.");
		
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
	@Test
	void completeTodoSuccessful() {
		// given
		Long todoId = 1L;
		LocalDate now = LocalDate.now();
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		PriorityEntity priorityEntity = craetePriorityEntity(Level.MID);
		TodoEntity todoEntity = createTodoEntity(userEntity, priorityEntity,"TODO LIST..", now.plusDays(1), now.plusDays(2), LocalDateTime.now()); 
		
		Mockito.when(todoRepository.findById(todoId)).thenReturn(Optional.of(todoEntity));
		
		// when
		todoService.completeTodo(todoId);
		
		// then
		assertThat(todoEntity.getTodoStatus()).isEqualTo(TodoStatus.COMPLETED);
		
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
	@Test
	void cancelTodoWhenTodoNotFound() {
		// given
		Long todoId = 1L;
		Mockito.when(todoRepository.findById(todoId)).thenReturn(Optional.empty());
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.cancelTodo(todoId));
		assertThat(exception.getMessage()).isEqualTo("Not Found TodoEntity.");
		
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
	@Test
	void cancelTodoSuccessful() {
		// given
		Long todoId = 1L;
		LocalDate now = LocalDate.now();
		PriorityEntity priorityEntity = craetePriorityEntity(Level.MID);
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		TodoEntity todoEntity = createTodoEntity(userEntity, priorityEntity,"TODO LIST..", now.plusDays(1), now.plusDays(2), LocalDateTime.now()); 
		
		Mockito.when(todoRepository.findById(todoId)).thenReturn(Optional.of(todoEntity));
		
		// when
		todoService.cancelTodo(todoId);
		
		// then
		assertThat(todoEntity.getTodoStatus()).isEqualTo(TodoStatus.CANCELLED);
		
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
	@Test
	void changeTodoPriorityWhenPriorityNotFound() {
		// given
		Long todoId = 1L;
		Level changeLevel = Level.LOW;
		
		Mockito.when(priorityRepository.findByLevel(changeLevel)).thenReturn(Optional.empty());
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.changeTodoPriority(todoId, changeLevel));
		assertThat(exception.getMessage()).isEqualTo("Not Found Priority Level.");
		
		Mockito.verify(priorityRepository, Mockito.times(1)).findByLevel(changeLevel);
		Mockito.verify(todoRepository, Mockito.times(0)).findById(todoId);
	}
	
	@Test
	void changeTodoPriorityWhenTodoNotFound() {
		// given
		Long todoId = 1L;
		Level changeLevel = Level.LOW;
		
		PriorityEntity priorityEntity = craetePriorityEntity(changeLevel);
		
		Mockito.when(priorityRepository.findByLevel(changeLevel)).thenReturn(Optional.of(priorityEntity));
		Mockito.when(todoRepository.findById(todoId)).thenReturn(Optional.empty());
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.changeTodoPriority(todoId, changeLevel));
		assertThat(exception.getMessage()).isEqualTo("Not Found TodoEntity.");
		
		Mockito.verify(priorityRepository, Mockito.times(1)).findByLevel(changeLevel);
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
	@Test
	void changeTodoPrioritySuccessful() {
		// given
		Long todoId = 1L;
		Level changeLevel = Level.HIGH;
		LocalDate now = LocalDate.now();
		
		PriorityEntity findPriorityEntity = craetePriorityEntity(changeLevel);
		
		PriorityEntity priorityEntity = craetePriorityEntity(Level.MID);
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		TodoEntity todoEntity = createTodoEntity(userEntity, priorityEntity,"TODO LIST..", now.plusDays(1), now.plusDays(2), LocalDateTime.now()); 
		
		Mockito.when(priorityRepository.findByLevel(changeLevel)).thenReturn(Optional.of(findPriorityEntity));
		Mockito.when(todoRepository.findById(todoId)).thenReturn(Optional.of(todoEntity));
		
		// when
		todoService.changeTodoPriority(todoId, changeLevel);
		
		// then
		assertThat(todoEntity.getPriorityEntity().getLevel()).isEqualTo(changeLevel);
		
		Mockito.verify(priorityRepository, Mockito.times(1)).findByLevel(changeLevel);
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
}
