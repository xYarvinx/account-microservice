package com.example.accountmicroservice.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}
