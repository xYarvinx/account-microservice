package com.example.accountmicroservice.dto;

import lombok.Data;

@Data
public class SignInRequestDto {
    private String username;
    private String password;
}
