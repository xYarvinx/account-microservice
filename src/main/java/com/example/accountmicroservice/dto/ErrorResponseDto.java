package com.example.accountmicroservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponseDto {
    private ErrorDto error;
}
