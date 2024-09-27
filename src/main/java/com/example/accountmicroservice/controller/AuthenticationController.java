package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.models.UserEntity;
import com.example.accountmicroservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/SignUp")
    public ResponseEntity register(@RequestBody SignUpRequest request) {
        try {
            authenticationService.signUp(request);
        } catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Вы успешно зарегестрировались!");
    }
}
