package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.TokenProvider;
import com.example.accountmicroservice.dto.TokenValidationRequest;
import com.example.accountmicroservice.dto.TokenValidationResponse;
import com.example.accountmicroservice.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitService {
    private final TokenProvider tokenProvider;
    private final RabbitTemplate rabbitTemplate;
    private final BlacklistTokenService blacklistTokenService;

    public RabbitService(TokenProvider tokenProvider, RabbitTemplate rabbitTemplate, BlacklistTokenService blacklistTokenService) {
        this.tokenProvider = tokenProvider;
        this.rabbitTemplate = rabbitTemplate;
        this.blacklistTokenService =  blacklistTokenService;
    }

    @RabbitListener(queues = "authRequestQueue")
    public void validateToken(TokenValidationRequest request) {
        boolean isValid = false;
        if(!blacklistTokenService.isBlacklisted(request.getToken()) && tokenProvider.validateToken(request.getToken())) {
            isValid = true;
        }
        TokenValidationResponse response = new TokenValidationResponse(isValid, request.getCorrelationId());

        rabbitTemplate.convertAndSend("authExchange", "auth.response." + request.getCorrelationId(), response);
    }


    @RabbitListener(queues = "roleRequestQueue")
    public void validateIsAdmin(TokenValidationRequest request) {
        boolean isAdmin = false;
        List<Role> role =  tokenProvider.getRolesFromToken(request.getToken());
        if (role.contains(Role.ADMIN)) {
            isAdmin = true;
        }
        TokenValidationResponse response = new TokenValidationResponse(isAdmin, request.getCorrelationId());

        rabbitTemplate.convertAndSend("roleExchange", "role.response." + request.getCorrelationId(), response);
    }
}

