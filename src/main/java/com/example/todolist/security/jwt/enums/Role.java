package com.example.todolist.security.jwt.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Role {

	ROLE_USER,
	ROLE_ADMIN;
	
	public String getAuthority() {
        return name();
    }
	
    public static boolean isValidRole(String roleName) {
        Set<String> roleNames = new HashSet<>();
        Arrays.stream(Role.values()).forEach(role -> roleNames.add(role.name()));
        return roleNames.contains(roleName);
    }
}
