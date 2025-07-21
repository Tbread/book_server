package com.tbread.book.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("ROLE_USER"),ADMIN("ROLE_ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
