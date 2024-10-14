package com.example.accountmicroservice.dto;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse implements Serializable {
    private boolean valid;
    private String correlationId;
}
