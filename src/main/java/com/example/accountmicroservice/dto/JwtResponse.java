package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Schema(description = "Ответ с JWT токенами")
public class JwtResponse {
    @Schema(description = "JWT access-токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String accessToken;

    @Schema(description = "JWT refresh-токен", example = "dGhpcyByZWZyZXNoIHRva2VuIHZhbHVl", required = true)
    private String refreshToken;

    @Schema(description = "Тип токена", example = "Bearer", defaultValue = "Bearer")
    private String tokenType = "Bearer";

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
