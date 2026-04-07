package com.reconix.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username e obrigatorio")
        String username,

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @NotBlank(message = "Senha e obrigatoria")
        String password
) {}