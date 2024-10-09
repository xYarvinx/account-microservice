package com.example.accountmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "Ответ, содержащий информацию об ошибке")
public class ErrorResponse {
    @Schema(description = "Информация об ошибке", required = true, implementation = Error.class)
    private Error error;
}
