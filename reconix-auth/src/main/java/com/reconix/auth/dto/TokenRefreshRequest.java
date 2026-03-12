package com.reconix.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para renovacao do token")
public record TokenRefreshRequest(

        @Schema(description = "Refresh token obtido no login")
        @NotBlank(message = "Refresh token e obrigatorio")
        String refreshToken
) {}