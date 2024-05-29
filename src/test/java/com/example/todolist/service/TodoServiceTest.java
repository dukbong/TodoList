package com.example.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
		assertThat(userEntity.getTodos().get(0).getTodoStatus()).isEqualTo(TodoStatus.PENDING);
		
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
		assertThat(todoEntity.getTodoStatus()).isEqualTo(TodoStatus.PENDING);
		
		Mockito.verify(priorityRepository, Mockito.times(1)).findByLevel(changeLevel);
		Mockito.verify(todoRepository, Mockito.times(1)).findById(todoId);
	}
	
	@Test
	void getTodosSuccessful() {
		// given
		Long userId = 1L;
		LocalDate now = LocalDate.now();
		
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		PriorityEntity priorityEntity = craetePriorityEntity(Level.MID);
		
		TodoEntity todoEntity1 = createTodoEntity(userEntity, priorityEntity,"TODO LIST..1", now.plusDays(1), now.plusDays(2), LocalDateTime.now()); 
		TodoEntity todoEntity2 = createTodoEntity(userEntity, priorityEntity,"TODO LIST..2", now.plusDays(2), now.plusDays(4), LocalDateTime.now()); 
		
		Mockito.when(todoRepository.getTodos(userId)).thenReturn(List.of(todoEntity1, todoEntity2));
		
		// when
		List<Todo> todos = todoService.getTodos(userId);
		
		// then
		assertThat(todos).isNotNull();
		assertThat(todos.size()).isEqualTo(2);
		
		Todo getTodo1 = todos.get(0);
		assertThat(getTodo1.getContent()).isEqualTo("TODO LIST..1");
		assertThat(getTodo1.getStartLine()).isEqualTo(now.plusDays(1));
		assertThat(getTodo1.getDeadLine()).isEqualTo(now.plusDays(2));
		assertThat(getTodo1.getTodoStatus()).isEqualTo(TodoStatus.PENDING);
		
		Todo getTodo2 = todos.get(1);
		assertThat(getTodo2.getContent()).isEqualTo("TODO LIST..2");
		assertThat(getTodo2.getStartLine()).isEqualTo(now.plusDays(2));
		assertThat(getTodo2.getDeadLine()).isEqualTo(now.plusDays(4));
		assertThat(getTodo2.getTodoStatus()).isEqualTo(TodoStatus.PENDING);
		
		Mockito.verify(todoRepository, Mockito.times(1)).getTodos(userId);
	}
	
	@Test
	void getBetweenTodosWhenStartIsAfterBefore() {
		// given
		Long userId = 1L;
		LocalDate now = LocalDate.now();
		LocalDate start = now.plusDays(2);
		LocalDate end = now.plusDays(1);
		
		// when, then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> todoService.getBetweenTodos(userId, start, end));
		assertThat(exception.getMessage()).isEqualTo("The start date cannot be greater than the end date.");
		Mockito.verify(todoRepository, Mockito.times(0)).getBetweenTodos(userId, start, end);
	}
	
	@Test
	void getBetweenTodosSuccessful() {
		// given
		Long userId = 1L;
		LocalDate now = LocalDate.now();
		LocalDate start = now.plusDays(1);
		LocalDate end = now.plusDays(3);
		
		UserEntity userEntity = createUserEntity("dukbong", "password", "ROLE_ADMIN");
		PriorityEntity priorityEntity = craetePriorityEntity(Level.MID);
		
		TodoEntity todoEntity1 = createTodoEntity(userEntity, priorityEntity,"TODO LIST..1", now.plusDays(1), now.plusDays(2), LocalDateTime.now()); 
		TodoEntity todoEntity2 = createTodoEntity(userEntity, priorityEntity,"TODO LIST..2", now.plusDays(2), now.plusDays(4), LocalDateTime.now()); 
		TodoEntity todoEntity3 = createTodoEntity(userEntity, priorityEntity,"TODO LIST..3", now.plusDays(2), now.plusDays(4), LocalDateTime.now()); 
		TodoEntity todoEntity4 = createTodoEntity(userEntity, priorityEntity,"TODO LIST..4", now.plusDays(4), now.plusDays(4), LocalDateTime.now()); 
		
		List<TodoEntity> todoEntities = Arrays.asList(todoEntity1, todoEntity2, todoEntity3);
		
		Mockito.when(todoRepository.getBetweenTodos(userId, start, end)).thenReturn(todoEntities);
		
		// when
		List<Todo> todos = todoService.getBetweenTodos(userId, start, end);
		
		// then
        assertThat(todos).hasSize(3); // Check only 3 todos within the date range
        assertThat(todos.get(0).getContent()).isEqualTo("TODO LIST..1");
        assertThat(todos.get(1).getContent()).isEqualTo("TODO LIST..2");
        assertThat(todos.get(2).getContent()).isEqualTo("TODO LIST..3");
        
        Mockito.verify(todoRepository, Mockito.times(1)).getBetweenTodos(userId, start, end);
	}
	
}
