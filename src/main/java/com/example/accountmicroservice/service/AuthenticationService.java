package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.JwtAuthentication;
import com.example.accountmicroservice.exception.AccountExistException;
import com.example.accountmicroservice.exception.InvalidDataException;
import com.example.accountmicroservice.exception.InvalidTokenException;
import com.example.accountmicroservice.dto.JwtResponse;
import com.example.accountmicroservice.dto.SignInRequest;
import com.example.accountmicroservice.dto.SignUpRequest;
import com.example.accountmicroservice.models.AccountEntity;
import com.example.accountmicroservice.config.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final BlacklistTokenService blacklistTokenService;
    private final RefreshTokenService refreshTokenService;

    public void signUp(SignUpRequest request) {
        if (accountService.existsByUsername(request.getUsername())) {
            throw new AccountExistException("Пользователь с таким username уже существует.");
        }
        accountService.createAccount(request);
    }


    public JwtResponse signIn(SignInRequest request) {
       AccountEntity account = accountService.getAccount(request.getUsername());

        if(passwordEncoder.matches(request.getPassword(), account.getPassword())){
            final String accessToken = tokenProvider.generateAccessToken(account);
            final String refreshToken = tokenProvider.generateRefreshToken(account);

            if(refreshTokenService.getRefreshToken(account.getUsername()) != null){
                refreshTokenService.deleteRefreshToken(account.getUsername());
            }
            refreshTokenService.saveRefreshToken(account.getUsername(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new InvalidDataException("Неверный логин или пароль");
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
        if(!refreshTokenService.validateRefreshToken(refreshToken)){
            throw new InvalidTokenException("Неверный токен обновления");
        }
        String username = tokenProvider.getClaims(refreshToken).getSubject();

        AccountEntity account = accountService.getAccount(username);

        final String newAccessToken = tokenProvider.generateAccessToken(account);
        final String newRefreshToken = tokenProvider.generateRefreshToken(account);

        refreshTokenService.deleteRefreshToken(username);
        refreshTokenService.saveRefreshToken(username, newRefreshToken);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }
}
