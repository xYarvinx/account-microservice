package com.example.accountmicroservice.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class BlacklistTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    public void addToBlacklist(String token, long expirationDuration) {
        redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofMillis(expirationDuration));
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
