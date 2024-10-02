package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.AccountResponse;
import com.example.accountmicroservice.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/Me")
    public ResponseEntity<?> infoMe(){
        AccountResponse info = accountService.infoMe();
        return ResponseEntity.ok(info);
    }


}
