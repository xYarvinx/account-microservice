package com.example.accountmicroservice.service;

import com.example.accountmicroservice.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public void signUp(SignUpRequest request) {
        if(userService.getByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Такой пользователь уже существует");
        }


    }
}
