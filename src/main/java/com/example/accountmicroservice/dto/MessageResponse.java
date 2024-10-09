package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с текстовым сообщением")
public class MessageResponse {
    @Schema(description = "Текст сообщения", example = "Вы успешно зарегистрировались!", required = true)
    private String message;
}
