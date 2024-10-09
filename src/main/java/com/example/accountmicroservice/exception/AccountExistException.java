package com.example.accountmicroservice.exception;

import org.springframework.http.HttpStatus;

public class AccountExistException extends ApiException{
    public AccountExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
