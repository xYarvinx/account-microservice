package com.example.accountmicroservice.config;

import com.example.accountmicroservice.dto.Error;
import com.example.accountmicroservice.dto.ErrorResponse;
import com.example.accountmicroservice.service.BlacklistTokenService;
import com.example.accountmicroservice.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private final BlacklistTokenService blacklistTokenService;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException, java.io.IOException {
        final String token = tokenProvider.extractToken((HttpServletRequest) request);
        if (token != null) {
            if (!tokenProvider.validateToken(token)) {
                sendErrorResponse(((HttpServletResponse) response),HttpServletResponse.SC_UNAUTHORIZED,"Token is invalid");
                return;
            }
            if (blacklistTokenService.isBlacklisted(token)) {
                sendErrorResponse(((HttpServletResponse) response),HttpServletResponse.SC_UNAUTHORIZED,"Token has been revoke");
                return;
            }
            final Claims claims = tokenProvider.getClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtil.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(Error.builder()
                        .message(message)
                        .build())
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
