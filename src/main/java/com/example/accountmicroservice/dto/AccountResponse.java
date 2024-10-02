package com.example.accountmicroservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Set<String> roles;
}
