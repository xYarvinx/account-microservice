package com.example.accountmicroservice.dto;

import lombok.Data;

@Data
public class UpdateAccountRequestDto {
    private String lastName;
    private String firstName;
    private String password;
}
