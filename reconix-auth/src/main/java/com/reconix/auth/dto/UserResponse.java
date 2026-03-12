package com.reconix.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Dados do usuario")
public record UserResponse(

        @Schema(description = "ID unico do usuario no Keycloak")
        String id,

        @Schema(description = "Nome de usuario")
        String username,

        @Schema(description = "Email")
        String email,

        @Schema(description = "Primeiro nome")
        String firstName,

        @Schema(description = "Sobrenome")
        String lastName,

        @Schema(description = "Se o usuario esta ativo")
        boolean enabled,

        @Schema(description = "Roles atribuidas ao usuario")
        List<String> roles
) {}