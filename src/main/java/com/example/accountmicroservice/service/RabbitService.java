package com.example.accountmicroservice.service;

import com.example.accountmicroservice.config.TokenProvider;
import com.example.accountmicroservice.dto.RoleValidationRequest;
import com.example.accountmicroservice.dto.RoleValidationResponse;
import com.example.accountmicroservice.dto.TokenValidationRequest;
import com.example.accountmicroservice.dto.TokenValidationResponse;
import com.example.accountmicroservice.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RabbitService {
    private final TokenProvider tokenProvider;
    private final RabbitTemplate rabbitTemplate;
    private final AccountService accountService;

    public RabbitService(TokenProvider tokenProvider, RabbitTemplate rabbitTemplate, AccountService accountService) {
        this.tokenProvider = tokenProvider;
        this.rabbitTemplate = rabbitTemplate;
        this.accountService = accountService;
    }

    @RabbitListener(queues = "authRequestQueue")
    public void validateToken(TokenValidationRequest request) {
        boolean isValid = tokenProvider.validateToken(request.getToken());
        TokenValidationResponse response = new TokenValidationResponse(isValid, request.getCorrelationId());

        rabbitTemplate.convertAndSend("authExchange", "auth.response." + request.getCorrelationId(), response);
    }


    @RabbitListener(queues = "roleRequestQueue")
    public void validateRole(RoleValidationRequest request) {
        List<Role> roles = tokenProvider.getRolesFromToken(request.getToken());
        boolean hasRole = false;

        for (String role : request.getRolesToCheck()) {
            Role roleToCheck = Role.valueOf(role);
            if (roles.contains(roleToCheck)) {
                hasRole = true;
                break;
            }
        }

        RoleValidationResponse response = new RoleValidationResponse(hasRole, request.getCorrelationId());

        rabbitTemplate.convertAndSend("roleExchange", "role.response." + request.getCorrelationId(), response);
    }

    @RabbitListener(queues = "userExistRequestQueue")
    public Boolean handleUserExistenceRequest(Map<String, Object> message) {
        Long userId = ((Number) message.get("userId")).longValue();
        String role = (String) message.get("role");

        return accountService.userExistsWithRole(userId, role);
    }

    @RabbitListener(queues = "userIdByTokenRequestQueue")
    public Long getUserIdByToken(String token) {
        String username = tokenProvider.getUsernameFromToken(token);
        return accountService.getAccount(username).getId();
    }
}

