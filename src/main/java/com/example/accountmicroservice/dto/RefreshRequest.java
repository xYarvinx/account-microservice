package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос для обновления JWT access-токена с помощью refresh-токена")
public class RefreshRequest {
    @Schema(description = "JWT refresh-токен", example = "dGhpcyByZWZyZXNoIHRva2VuIHZhbHVl", required = true)
    private String refreshToken;
}
