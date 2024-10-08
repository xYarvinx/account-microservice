package com.example.accountmicroservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AccountRequest {
    private String lastName;
    private String firstName;
    private String username;
    private String password;
    private Set<String> roles;
}
