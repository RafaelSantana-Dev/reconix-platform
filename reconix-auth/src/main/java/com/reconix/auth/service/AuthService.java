package com.reconix.auth.service;

import com.reconix.auth.dto.LoginRequest;
import com.reconix.auth.dto.LoginResponse;
import com.reconix.auth.dto.LogoutRequest;
import com.reconix.auth.dto.TokenRefreshRequest;
import com.reconix.auth.exception.AuthenticationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final RestClient keycloakRestClient;

    public AuthService(RestClient keycloakRestClient) {
        this.keycloakRestClient = keycloakRestClient;
    }

    @SuppressWarnings("unchecked")
    public LoginResponse login(LoginRequest request) {
        String formBody = "grant_type=password"
                + "&client_id=reconix-backend"
                + "&username=" + request.username()
                + "&password=" + request.password();

        try {
            Map<String, Object> response = keycloakRestClient.post()
                    .uri("/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formBody)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                throw new AuthenticationFailedException("Resposta vazia do Keycloak");
            }

            log.info("[AUTH-SERVICE] Login realizado com sucesso para: {}", request.username());

            return new LoginResponse(
                    (String) response.get("access_token"),
                    (String) response.get("refresh_token"),
                    (String) response.getOrDefault("token_type", "Bearer"),
                    ((Number) response.getOrDefault("expires_in", 300)).longValue(),
                    (String) response.get("session_state"),
                    (String) response.get("scope")
            );

        } catch (RestClientResponseException e) {
            log.warn("[AUTH-SERVICE] Falha no login para: {} | Status: {}", request.username(), e.getStatusCode());
            throw new AuthenticationFailedException("Credenciais invalidas");
        }
    }

    @SuppressWarnings("unchecked")
    public LoginResponse refreshToken(TokenRefreshRequest request) {
        String formBody = "grant_type=refresh_token"
                + "&client_id=reconix-backend"
                + "&refresh_token=" + request.refreshToken();

        try {
            Map<String, Object> response = keycloakRestClient.post()
                    .uri("/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formBody)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                throw new AuthenticationFailedException("Resposta vazia do Keycloak");
            }

            log.info("[AUTH-SERVICE] Token renovado com sucesso");

            return new LoginResponse(
                    (String) response.get("access_token"),
                    (String) response.get("refresh_token"),
                    (String) response.getOrDefault("token_type", "Bearer"),
                    ((Number) response.getOrDefault("expires_in", 300)).longValue(),
                    (String) response.get("session_state"),
                    (String) response.get("scope")
            );

        } catch (RestClientResponseException e) {
            log.warn("[AUTH-SERVICE] Falha ao renovar token | Status: {}", e.getStatusCode());
            throw new AuthenticationFailedException("Refresh token invalido ou expirado");
        }
    }

    public void logout(LogoutRequest request) {
        String formBody = "client_id=reconix-backend"
                + "&refresh_token=" + request.refreshToken();

        try {
            keycloakRestClient.post()
                    .uri("/protocol/openid-connect/logout")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formBody)
                    .retrieve()
                    .toBodilessEntity();

            log.info("[AUTH-SERVICE] Logout realizado com sucesso");

        } catch (RestClientResponseException e) {
            log.warn("[AUTH-SERVICE] Falha no logout | Status: {}", e.getStatusCode());
            throw new AuthenticationFailedException("Falha ao realizar logout");
        }
    }
}