package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию нового пользователя")
public class SignUpRequest {
    @Schema(description = "Фамилия пользователя", example = "Doe", required = true)
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @Schema(description = "Имя пользователя", example = "John", required = true)
    @NotBlank(message = "Требуется имя")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов.")
    private String firstName;

    @Schema(description = "Уникальное имя пользователя для учетной записи", example = "johndoe", required = true)
    @NotBlank(message = "Требуется имя пользователя")
    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов.")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password123", required = true)
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 4, message = "Пароль должен быть не менее 4 символов")
    private String password;
}
