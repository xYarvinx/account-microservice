package com.example.accountmicroservice.exception;

import com.example.accountmicroservice.dto.ErrorDto;
import com.example.accountmicroservice.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = ControllerExceptionHandler.class)
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponseDto> handleApiException(ApiException e) {
        ErrorDto error = ErrorDto.builder()
                .message(e.getMessage())
                .build();

        ErrorResponseDto result = new ErrorResponseDto().setError(error);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(result);
    }
}
