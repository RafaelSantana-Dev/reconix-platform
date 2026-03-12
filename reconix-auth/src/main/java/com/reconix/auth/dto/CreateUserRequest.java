package com.reconix.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Dados para criacao de um novo usuario")
public record CreateUserRequest(

        @Schema(description = "Nome de usuario", example = "joao.silva")
        @NotBlank(message = "Username e obrigatorio")
        @Size(min = 3, max = 100, message = "Username deve ter entre 3 e 100 caracteres")
        String username,

        @Schema(description = "Email do usuario", example = "joao@empresa.com")
        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @Schema(description = "Primeiro nome", example = "Joao")
        @NotBlank(message = "Primeiro nome e obrigatorio")
        String firstName,

        @Schema(description = "Sobrenome", example = "Silva")
        @NotBlank(message = "Sobrenome e obrigatorio")
        String lastName,

        @Schema(description = "Senha (minimo 8 caracteres)", example = "senhaSegura123")
        @NotBlank(message = "Senha e obrigatoria")
        @Size(min = 8, message = "Senha deve ter no minimo 8 caracteres")
        String password,

        @Schema(description = "Lista de roles a atribuir", example = "[\"ROLE_ANALYST\"]")
        List<String> roles
) {}