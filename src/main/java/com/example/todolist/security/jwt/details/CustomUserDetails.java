package com.example.todolist.security.jwt.details;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.todolist.dto.TokenInfo;

public class CustomUserDetails implements UserDetails{
	
	private final TokenInfo tokenInfo;
	
	public CustomUserDetails(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> role = new ArrayList<>();
		role.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return tokenInfo.getRole().name();
			}
		});
		
		return role;
	}

	@Override
	public String getPassword() {
		return tokenInfo.getPassword();
	}

	@Override
	public String getUsername() {
		return tokenInfo.getUsername();
	}

}
