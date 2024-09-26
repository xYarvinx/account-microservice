package com.example.accountmicroservice.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Authentication")
public class AuthController  {

    @GetMapping("/SignUp")
    public String register() {
        return "Hello World!";
    }
}
