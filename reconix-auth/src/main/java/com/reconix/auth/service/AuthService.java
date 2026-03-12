package com.reconix.auth.service;

import com.reconix.auth.config.KeycloakProperties;
import com.reconix.auth.dto.LoginRequest;
import com.reconix.auth.dto.LoginResponse;
import com.reconix.auth.dto.LogoutRequest;
import com.reconix.auth.dto.TokenRefreshRequest;
import com.reconix.auth.exception.AuthenticationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final RestClient keycloakRestClient;
    private final KeycloakProperties keycloakProperties;

    public AuthService(RestClient keycloakRestClient, KeycloakProperties keycloakProperties) {
        this.keycloakRestClient = keycloakRestClient;
        this.keycloakProperties = keycloakProperties;
    }

    /**
     * Autentica o usuario no Keycloak e retorna os tokens JWT.
     */
    public LoginResponse login(LoginRequest request) {
        log.info("[AUTH] Tentativa de login para usuario: {}", request.username());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", keycloakProperties.clientId());
        formData.add("client_secret", keycloakProperties.clientSecret());
        formData.add("username", request.username());
        formData.add("password", request.password());
        formData.add("scope", "openid");

        try {
            LoginResponse response = keycloakRestClient.post()
                    .uri(keycloakProperties.tokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(LoginResponse.class);

            log.info("[AUTH] Login realizado com sucesso para usuario: {}", request.username());
            return response;

        } catch (RestClientResponseException e) {
            log.warn("[AUTH] Falha no login para usuario: {} | Status: {}",
                    request.username(), e.getStatusCode());
            throw new AuthenticationFailedException("Credenciais invalidas");
        }
    }

    /**
     * Renova o access token usando o refresh token.
     */
    public LoginResponse refreshToken(TokenRefreshRequest request) {
        log.info("[AUTH] Tentativa de refresh token");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", keycloakProperties.clientId());
        formData.add("client_secret", keycloakProperties.clientSecret());
        formData.add("refresh_token", request.refreshToken());

        try {
            LoginResponse response = keycloakRestClient.post()
                    .uri(keycloakProperties.tokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(LoginResponse.class);

            log.info("[AUTH] Token renovado com sucesso");
            return response;

        } catch (RestClientResponseException e) {
            log.warn("[AUTH] Falha ao renovar token | Status: {}", e.getStatusCode());
            throw new AuthenticationFailedException("Refresh token invalido ou expirado");
        }
    }

    /**
     * Invalida a sessao do usuario no Keycloak.
     */
    public void logout(LogoutRequest request) {
        log.info("[AUTH] Tentativa de logout");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", keycloakProperties.clientId());
        formData.add("client_secret", keycloakProperties.clientSecret());
        formData.add("refresh_token", request.refreshToken());

        try {
            keycloakRestClient.post()
                    .uri(keycloakProperties.logoutUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .toBodilessEntity();

            log.info("[AUTH] Logout realizado com sucesso");

        } catch (RestClientResponseException e) {
            log.warn("[AUTH] Falha no logout | Status: {}", e.getStatusCode());
            throw new AuthenticationFailedException("Falha ao realizar logout");
        }
    }
}