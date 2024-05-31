package com.example.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import com.example.todolist.dto.Join;
import com.example.todolist.entity.UserEntity;
import com.example.todolist.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BasicServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	BasicService basicService;
	
	@Test
	void joinProcessWhenUsernameDuplicate() {
		// given
		Join join = Join.builder().username("dukbong").password("asdfASDF1!").email("email@email.com").build();
		Mockito.when(userRepository.existsByUsername(join.getUsername())).thenReturn(true);
		
		// when & then
		DuplicateKeyException exception = Assertions.assertThrows(DuplicateKeyException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("This username already exists.");
		Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(join.getUsername());
		Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
	}
	@Test
	void joinProcessWhenPasswordEmpty() {
		// given
		Join join = Join.builder().username("dukbong").email("email@email.com").build();
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("Password must be at least 8 characters.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessWhenPasswordNull() {
		// given
		Join join = Join.builder().username("dukbong").password(null).email("email@email.com").build();
				
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("Password must be at least 8 characters.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessWhenPasswordLeast8() {
		// given
		Join join = Join.builder().username("dukbong").password("1234567").email("email@email.com").build();
					
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("Password must be at least 8 characters.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessWhenPasswordContainUpperCase() {
		// given
		Join join = Join.builder().username("dukbong").password("asdf1234!").email("email@email.com").build();
					
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("The password must contain at least one uppercase letter.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessWhenPasswordContainLowerCase() {
		// given
		Join join = Join.builder().username("dukbong").password("ASDF123!").email("email@email.com").build();
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("The password must contain at least one lowercase letter.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessWhenPasswordContainSpecialCharacter() {
		// given
		Join join = Join.builder().username("dukbong").password("asdfASDF1").email("email@email.com").build();
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("The password must contain at least one special character.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessWhenPasswordOnlyNumber() {
		// given
		Join join = Join.builder().username("dukbong").password("12345678").email("email@email.com").build();
		
		// when & then
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> basicService.joinProcess(join));
		assertThat(exception.getMessage()).isEqualTo("The password must contain at least one uppercase letter.");
		Mockito.verify(userRepository, Mockito.never()).existsByUsername(join.getUsername());
	}
	@Test
	void joinProcessSuccessful() {
		// given
		Join join = Join.builder().username("dukbong").password("asdfASDF1!").email("email@email.com").build();
		Mockito.when(userRepository.existsByUsername(join.getUsername())).thenReturn(false);
		
		// when
		basicService.joinProcess(join);
		
		// then
		Mockito.verify(userRepository, Mockito.times(1)).existsByUsername(join.getUsername());
		Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
	}
}
