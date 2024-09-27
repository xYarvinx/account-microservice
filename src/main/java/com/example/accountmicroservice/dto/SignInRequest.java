package com.example.accountmicroservice.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
