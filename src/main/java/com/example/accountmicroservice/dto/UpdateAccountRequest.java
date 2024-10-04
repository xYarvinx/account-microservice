package com.example.accountmicroservice.dto;

import lombok.Data;

@Data
public class UpdateAccountRequest {
    private String lastName;
    private String firstName;
    private String password;
}
