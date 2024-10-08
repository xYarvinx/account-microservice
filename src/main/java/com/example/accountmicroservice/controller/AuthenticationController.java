package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.JwtResponse;
import com.example.accountmicroservice.dto.RefreshRequest;
import com.example.accountmicroservice.dto.SignInRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
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
            return ResponseEntity.ok().body("{\"message\": \"Вы успешно зарегестрировались!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/SignIn")
    public ResponseEntity<?> login(@RequestBody SignInRequest request) {
        try {
            JwtResponse tokens = authenticationService.signIn(request);
            return ResponseEntity.ok().body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/SignOut")
    public ResponseEntity<?> logout(HttpServletRequest request){
        authenticationService.signOut(request);
        return  ResponseEntity.ok().body("{\"message\": \"Вы успешно вышли из аккаунта!\"}");
    }

    @GetMapping("/Validate")
    public ResponseEntity<?> validate(@RequestParam String accessToken){
        Boolean isValid = authenticationService.validate(accessToken);
        return ResponseEntity.ok().body("{\"message\":" + isValid + "}");
    }

    @PostMapping("/Refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshToken){
        try {
            JwtResponse tokens = authenticationService.refresh(refreshToken.getRefreshToken());
            return ResponseEntity.ok().body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
