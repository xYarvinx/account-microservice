package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.JwtResponse;
import com.example.accountmicroservice.dto.SignInRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/SignUp")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {
        try {
            authenticationService.signUp(request);
            return ResponseEntity.status(HttpStatus.OK).body("Вы успешно зарегестрировались!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/SignIn")
    public ResponseEntity<?> login(@RequestBody SignInRequest request) {
        try {
            JwtResponse tokens = authenticationService.signIn(request);
            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/SignOut")
    public ResponseEntity<?> logout(HttpServletRequest request){
        try {
            authenticationService.signOut(request);
            return  ResponseEntity.status(HttpStatus.OK).body("Вы успешно, вышли из акаунта");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
