package com.example.accountmicroservice.model;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER"),
    DOCTOR("DOCTOR"),
    MANAGER("MANAGER");

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
