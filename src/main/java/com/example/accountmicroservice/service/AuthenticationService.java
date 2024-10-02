package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.JwtAuthentication;
import com.example.accountmicroservice.config.TokenProvider;
import com.example.accountmicroservice.dto.JwtResponse;
import com.example.accountmicroservice.dto.SignInRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.models.AccountEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final BlacklistTokenService blacklistTokenService;
    private final RefreshTokenService refreshTokenService;

    public void signUp(SignUpRequest request) {
        if(accountService.getByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("{\"error\": \"Такой пользователь уже существует\"}");
        }
        accountService.createAccount(request);
    }


    public JwtResponse signIn(SignInRequest request) {
        Optional<AccountEntity> account = Optional.ofNullable(accountService
                    .getByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("{\"error\": \"Пользователь не найден\"}")));

        if( account.isPresent() && passwordEncoder.matches(request.getPassword(), account.get().getPassword())){
            final String accessToken = tokenProvider.generateAccessToken(account);
            final String refreshToken = tokenProvider.generateRefreshToken(account);

            if(refreshTokenService.getRefreshToken(account.get().getUsername()) != null){
                refreshTokenService.deleteRefreshToken(account.get().getUsername());
            }
            refreshTokenService.saveRefreshToken(account.get().getUsername(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new RuntimeException("{\"error\": \"Неверный логин или пароль\"}");
        }
    }



    public void signOut(HttpServletRequest request) {
        String token = tokenProvider.extractToken(request);
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


    public JwtResponse refresh(String refreshToken) {
        if(!tokenProvider.validateToken(refreshToken)){
            throw new RuntimeException("{\"error\": \"Неверный токен обновления\"}");
        }

        String username = tokenProvider.getClaims(refreshToken).getSubject();
        if(!refreshTokenService.validateRefreshToken(refreshToken)){
            throw new RuntimeException("{\"error\": \"Неверный токен обновления\"}");
        }

        Optional<AccountEntity> account = accountService.getByUsername(username);
        if(account.isEmpty()){
            throw new RuntimeException("{\"error\": \"Пользователь не найден\"}");
        }

        final String newAccessToken = tokenProvider.generateAccessToken(account);
        final String newRefreshToken = tokenProvider.generateRefreshToken(account);

        refreshTokenService.deleteRefreshToken(username);
        refreshTokenService.saveRefreshToken(username, newRefreshToken);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }
}
