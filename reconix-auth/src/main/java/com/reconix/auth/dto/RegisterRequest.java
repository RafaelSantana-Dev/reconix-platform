package com.reconix.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Nome Ã© obrigatÃ³rio")
        @Size(min = 3, max = 100)
        String name,

        @NotBlank(message = "Email Ã© obrigatÃ³rio")
        @Email(message = "Email invÃ¡lido")
        String email,

        @NotBlank(message = "Senha Ã© obrigatÃ³ria")
        @Size(min = 6, message = "Senha deve ter no mÃ­nimo 6 caracteres")
        String password,

        @NotBlank(message = "Nome do tenant Ã© obrigatÃ³rio")
        String tenantSlug
) {}