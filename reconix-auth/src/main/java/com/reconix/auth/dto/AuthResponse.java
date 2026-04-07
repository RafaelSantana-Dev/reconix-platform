package com.reconix.auth.domain.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        String tenantSlug,
        String role
) {}