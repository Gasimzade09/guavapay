package com.guavapay.task.security.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtAuthenticationResponse {

    private final String token;


    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }
}