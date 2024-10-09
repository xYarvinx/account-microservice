package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.dto.*;
import com.example.accountmicroservice.exception.ControllerExceptionHandler;
import com.example.accountmicroservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Authentication")
@RequiredArgsConstructor
@ControllerExceptionHandler
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/SignUp")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDto register(@RequestBody SignUpRequestDto request) {
        authenticationService.signUp(request);
        return new MessageResponseDto("Вы успешно зарегистрировались!");
    }

    @PostMapping("/SignIn")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto login(@RequestBody SignInRequestDto request) {
        return authenticationService.signIn(request);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/SignOut")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto logout(HttpServletRequest request){
        authenticationService.signOut(request);
        return new MessageResponseDto("Вы успешно вышли из системы!");
    }

    @GetMapping("/Validate")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDto validate(@RequestParam String accessToken){
        Boolean isValid = authenticationService.validate(accessToken);
        return new MessageResponseDto(isValid ? "Токен валиден" : "Токен не валиден");
    }

    @PostMapping("/Refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto refresh(@RequestBody RefreshRequestDto refreshToken){
        return authenticationService.refresh(refreshToken.getRefreshToken());
    }

}
