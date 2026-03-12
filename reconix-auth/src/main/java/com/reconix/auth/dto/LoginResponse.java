package com.reconix.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tokens JWT retornados apos autenticacao")
public record LoginResponse(

        @Schema(description = "Token de acesso JWT")
        @JsonProperty("access_token")
        String accessToken,

        @Schema(description = "Token para renovacao")
        @JsonProperty("refresh_token")
        String refreshToken,

        @Schema(description = "Tipo do token (Bearer)")
        @JsonProperty("token_type")
        String tokenType,

        @Schema(description = "Tempo de expiracao do access token em segundos")
        @JsonProperty("expires_in")
        int expiresIn,

        @Schema(description = "Tempo de expiracao do refresh token em segundos")
        @JsonProperty("refresh_expires_in")
        int refreshExpiresIn,

        @Schema(description = "Escopos autorizados")
        @JsonProperty("scope")
        String scope
) {}