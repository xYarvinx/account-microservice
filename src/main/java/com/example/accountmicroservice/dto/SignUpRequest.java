package com.example.accountmicroservice.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String lastName;
    private String firstName;
    private String username;
    private String password;
}
