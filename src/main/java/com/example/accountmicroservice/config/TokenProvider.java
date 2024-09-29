package com.example.accountmicroservice.config;

import com.example.accountmicroservice.models.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


@Component
public class TokenProvider {

    private final Key JWT_SECRET;
    private final long ACCESS_TOKEN_EXPIRATION;
    private final long REFRESH_TOKEN_EXPIRATION;

    public TokenProvider(
            @Value("${auth.jwt.secret}") String JWT_SECRET,
            @Value("${auth.jwt.access.expiration}") long ACCESS_TOKEN_EXPIRATION,
    @Value("${auth.jwt.refresh.expiration}") long REFRESH_TOKEN_EXPIRATION
    ) {
        this.JWT_SECRET = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
        this.ACCESS_TOKEN_EXPIRATION = ACCESS_TOKEN_EXPIRATION;
        this.REFRESH_TOKEN_EXPIRATION = REFRESH_TOKEN_EXPIRATION;
    }


    public String generateAccessToken(Optional<UserEntity> user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRATION)).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.get().getUsername())
                .setExpiration(accessExpiration)
                .signWith(JWT_SECRET)
                .claim("roles", user.get().getRoles())
                .claim("username", user.get().getUsername())
                .compact();
    }

    public String generateRefreshToken(@NonNull Optional<UserEntity> user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION)).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.get().getUsername())
                .setExpiration(refreshExpiration)
                .signWith(JWT_SECRET)
                .compact();
    }


    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }


    public Claims getClaims(@NonNull String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
