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

    public RabbitService(TokenProvider tokenProvider, RabbitTemplate rabbitTemplate) {
        this.tokenProvider = tokenProvider;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "authRequestQueue")
    public void validateToken(TokenValidationRequest request) {
        boolean isValid = tokenProvider.validateToken(request.getToken());
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

