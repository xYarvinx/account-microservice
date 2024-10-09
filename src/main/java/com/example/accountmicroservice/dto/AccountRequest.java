package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "Запрос на создание/обновление пользователя")
public class AccountRequest {
    @Schema(description = "Фамилия пользователя", example = "Doe", required = true)
    @NotBlank(message = "Требуется фамилия")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов.")
    private String lastName;

    @Schema(description = "Имя пользователя", example = "John", required = true)
    @NotBlank(message = "Требуется Имя")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов.")
    private String firstName;

    @Schema(description = "Уникальное имя пользователя для учетной записи", example = "johndoe", required = true)
    @NotBlank(message = "Требуется имя пользователя")
    @Size(min = 3, max = 30, message = "Имя пользователя должно содержать от 3 до 30 символов.")
    private String username;

    @Schema(description = "Пароль для учетной записи", example = "password123", required = true)
    @NotBlank(message = "Требуется пароль")
    @Size(min = 4, message = "Пароль должен содержать не менее 4 символов")
    private String password;

    @Schema(description = "Роли, назначенные учетной записи", example = "[\"USER\", \"ADMIN\"]", required = true)
    @NotNull(message = "Требуется роль")
    private Set<String> roles;
}
