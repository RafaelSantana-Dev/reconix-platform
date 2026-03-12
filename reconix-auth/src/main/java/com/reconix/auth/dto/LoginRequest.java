package com.reconix.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para autenticacao do usuario")
public record LoginRequest(

        @Schema(description = "Nome de usuario ou email", example = "admin@reconix.com")
        @NotBlank(message = "Username e obrigatorio")
        String username,

        @Schema(description = "Senha do usuario", example = "admin123")
        @NotBlank(message = "Password e obrigatorio")
        String password
) {}