package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    public void saveRefreshToken(String username, String refreshToken) {
        Date expirationDate = tokenProvider.getClaims(refreshToken).getExpiration();
        long expirationDuration = expirationDate.getTime() - new Date().getTime();
        redisTemplate.opsForValue().set("refresh_token:" + username, refreshToken, Duration.ofMillis(expirationDuration));
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("refresh_token:" + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh_token:" + username);
    }

    public boolean validateRefreshToken(String refreshToken) {
        String username = tokenProvider.getClaims(refreshToken).get("username", String.class);
        String storedToken = getRefreshToken(username);
        return storedToken != null && storedToken.equals(refreshToken) && tokenProvider.validateToken(refreshToken);
    }
}
