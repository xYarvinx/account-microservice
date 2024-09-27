package com.example.accountmicroservice.models;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER"),
    DOCTOR("DOCTOR"),
    MANAGER("MANAGER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
