package com.example.accountmicroservice.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFountException extends ApiException{
    public AccountNotFountException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
