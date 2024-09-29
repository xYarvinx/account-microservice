package com.example.accountmicroservice.dto;

import lombok.*;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
