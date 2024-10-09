package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@Schema(description = "Ответ с информацией о пользователе")
public class AccountResponse {
    @Schema(description = "Уникальный идентификатор пользователя", example = "1", required = true)
    private Long id;
    @Schema(description = "Имя пользователя", example = "John", required = true)
    private String firstName;
    @Schema(description = "Фамилия пользователя", example = "Doe", required = true)
    private String lastName;
    @Schema(description = "Уникальное имя пользователя для учетной записи", example = "johndoe", required = true)
    private String username;
    @Schema(description = "Роли, назначенные учетной записи", example = "[\"USER\", \"ADMIN\"]", required = true)
    private Set<String> roles;
}
