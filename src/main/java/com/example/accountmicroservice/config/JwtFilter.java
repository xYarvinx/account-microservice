package com.example.accountmicroservice.config;

import com.example.accountmicroservice.service.BlacklistTokenService;
import com.example.accountmicroservice.utils.JwtUtil;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException, java.io.IOException {
        final String token = tokenProvider.extractToken((HttpServletRequest) request);
        if (token != null) {
            if (!tokenProvider.validateToken(token)) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":{\"message\":\"Token is invalid\"}}");
                return;
            }
            if (blacklistTokenService.isBlacklisted(token)) {

                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":{\"message\":\"Token has been revoke\"}}");
                return;
            }
            final Claims claims = tokenProvider.getClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtil.generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        filterChain.doFilter(request, response);
    }
}
