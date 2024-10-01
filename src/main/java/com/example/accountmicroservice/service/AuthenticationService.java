package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.JwtAuthentication;
import com.example.accountmicroservice.config.TokenProvider;
import com.example.accountmicroservice.dto.JwtResponse;
import com.example.accountmicroservice.dto.SignInRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.models.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final BlacklistTokenService blacklistTokenService;
    private final RefreshTokenService refreshTokenService;

    public void signUp(SignUpRequest request) {
        if(userService.getByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("{\"error\": \"Такой пользователь уже существует\"}");
        }
        userService.createUser(request);
    }


    public JwtResponse signIn(SignInRequest request) {
        Optional<UserEntity> user = Optional.ofNullable(userService
                    .getByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("{\"error\": \"Пользователь не найден\"}")));

        if( user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
            final String accessToken = tokenProvider.generateAccessToken(user);
            final String refreshToken = tokenProvider.generateRefreshToken(user);

            if(refreshTokenService.getRefreshToken(user.get().getUsername()) != null){
                refreshTokenService.deleteRefreshToken(user.get().getUsername());
            }
            refreshTokenService.saveRefreshToken(user.get().getUsername(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new RuntimeException("{\"error\": \"Неверный логин или пароль\"}");
        }
    }



    public void signOut(HttpServletRequest request) {
        String token = extractToken(request);
        Date expirationDate = tokenProvider.getExpirationDateFromToken(token);
        long expirationTime = expirationDate.getTime() - new Date().getTime();

        blacklistTokenService.addToBlacklist(token, expirationTime);
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();

        refreshTokenService.deleteRefreshToken(jwtAuthentication.getUsername());
    }

    public Boolean validate(String accessToken) {
        if(blacklistTokenService.isBlacklisted(accessToken)){
            return false;
        }
        return tokenProvider.validateToken(accessToken);
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public JwtResponse refresh(String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken)){
            throw new RuntimeException("{\"error\": \"Неверный токен обновления\"}");
        }

        String username = tokenProvider.getClaims(refreshToken).getSubject();
        if(!refreshTokenService.getRefreshToken(username).equals(refreshToken)){
            throw new RuntimeException("{\"error\": \"Неверный токен обновления\"}");
        }

        Optional<UserEntity> user = userService.getByUsername(username);
        if(user.isEmpty()){
            throw new RuntimeException("{\"error\": \"Пользователь не найден\"}");
        }

        final String newAccessToken = tokenProvider.generateAccessToken(user);
        final String newRefreshToken = tokenProvider.generateRefreshToken(user);

        refreshTokenService.deleteRefreshToken(username);
        refreshTokenService.saveRefreshToken(username, newRefreshToken);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }
}
