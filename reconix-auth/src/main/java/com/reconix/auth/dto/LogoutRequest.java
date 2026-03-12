package com.reconix.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para logout e invalidacao de sessao")
public record LogoutRequest(

        @Schema(description = "Refresh token da sessao a ser invalidada")
        @NotBlank(message = "Refresh token e obrigatorio")
        String refreshToken
) {}