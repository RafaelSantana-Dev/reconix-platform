package com.reconix.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        String sessionState,
        String scope
) {}