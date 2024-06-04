package com.example.todolist.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.todolist.security.jwt.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
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
	
	private String email;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(length = 1)
	private String useYn;
	
	@OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
	private List<TodoEntity> todos = new ArrayList<>();
	
	@OneToOne
	@JoinColumn(name = "active_token_id")
	private ActiveTokenEntity activeTokenEntity;
	
	@OneToOne
	@JoinColumn(name = "refresh_token_id")
	private RefreshTokenEntity refreshTokenEntity;

	@Builder
	public UserEntity(String username, String email, String password, Role role, ActiveTokenEntity activeTokenEntity, RefreshTokenEntity refreshTokenEntity, String useYn) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.activeTokenEntity = activeTokenEntity;
		this.refreshTokenEntity = refreshTokenEntity;
		if(!"Y".equals(useYn) && !"N".equals(useYn)) {
            throw new IllegalArgumentException("useYn must be 'Y' or 'N'");
        }
        this.useYn = useYn;
	}
}
