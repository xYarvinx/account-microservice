package com.example.accountmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleValidationRequest {
    private String token;
    private String correlationId;
    private List<String> rolesToCheck;
}
