package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.JwtAuthentication;
import com.example.accountmicroservice.config.TokenProvider;
import com.example.accountmicroservice.dto.JwtResponse;
import com.example.accountmicroservice.dto.SignInRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.models.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;

    public void signUp(SignUpRequest request) {
        if(userService.getByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Такой пользователь уже существует");
        }
        userService.createUser(request);
    }


    public JwtResponse signIn(SignInRequest request) {
        Optional<UserEntity> user = Optional.ofNullable(userService
                    .getByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден")));

        if( user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            final String accessToken = tokenProvider.generateAccessToken(user);
            final String refreshToken = tokenProvider.generateRefreshToken(user);

            if(refreshTokenService.getRefreshToken(user.get().getUsername()) != null){
                refreshTokenService.deleteRefreshToken(user.get().getUsername());
            }
            refreshTokenService.saveRefreshToken(user.get().getUsername(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new RuntimeException("Неверный логин или пароль");
        }
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    public void signOut() {
    }
}
