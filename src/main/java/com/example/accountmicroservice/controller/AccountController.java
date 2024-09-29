package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Accounts")
@AllArgsConstructor
public class AccountController {
    private final RefreshTokenService refreshTokenService;


}
