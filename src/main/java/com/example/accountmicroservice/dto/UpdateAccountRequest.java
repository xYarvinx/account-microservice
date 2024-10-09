package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление данных пользователя")
public class UpdateAccountRequest {
    @Schema(description = "Фамилия пользователя", example = "Doe", required = true)
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @Schema(description = "Имя пользователя", example = "John", required = true)
    @NotBlank(message = "Требуется имя")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов.")
    private String firstName;

    @Schema(description = "Пароль пользователя", example = "password123", required = true)
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 4, message = "Пароль должен быть не менее 4 символов")
    private String password;
}
