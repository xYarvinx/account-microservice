package com.example.accountmicroservice.config;

import com.example.accountmicroservice.model.AccountEntity;
import com.example.accountmicroservice.model.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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


    public String generateAccessToken(AccountEntity account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRATION)).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(account.getUsername())
                .setExpiration(accessExpiration)
                .signWith(JWT_SECRET)
                .claim("roles", account.getRoles())
                .claim("firstname", account.getFirstName())
                .compact();
    }

    public String generateRefreshToken(@NonNull AccountEntity account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION)).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(account.getUsername())
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

    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }
    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public List<Role> getRolesFromToken(String token) {
        Claims claims = getClaims(token);

        List<String> rolesAsString = claims.get("roles", List.class);
        List<Role> roles = new ArrayList<>();

        if (rolesAsString != null) {
            for (String roleName : rolesAsString) {
                try {

                    Role role = Role.valueOf(roleName);
                    roles.add(role);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role found in token: " + roleName);
                }
            }
        }
        return roles;
    }
}
