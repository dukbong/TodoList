package com.example.todolist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name ="black_list_gen", sequenceName = "black_list_seq")
public class BlackListTokenEntity {

	@Id
	@GeneratedValue(generator = "black_list_gen", strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_entity_id")
	private UserEntity userEntity;
	
	private String blackListToken;
	
	@Builder
	public BlackListTokenEntity(UserEntity userEntity, String blackListToken) {
		this.userEntity = userEntity;
		this.blackListToken = blackListToken;
	}
	
}
