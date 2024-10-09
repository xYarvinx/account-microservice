package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход в систему")
public class SignInRequest {
    @Schema(description = "Уникальное имя пользователя для учетной записи", example = "johndoe", required = true)
    @NotBlank(message = "Требуется имя пользователя")
    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов.")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123", required = true)
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 4, message = "Пароль должен быть не менее 4 символов")
    private String password;
}
