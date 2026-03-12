package com.reconix.auth.controller;

import com.reconix.auth.dto.LoginRequest;
import com.reconix.auth.dto.LoginResponse;
import com.reconix.auth.dto.LogoutRequest;
import com.reconix.auth.dto.TokenRefreshRequest;
import com.reconix.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacao", description = "Login, logout, refresh token e informacoes do usuario")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login", description = "Autentica o usuario e retorna access_token + refresh_token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("[AUTH-CONTROLLER] Login request para: {}", request.username());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh Token", description = "Renova o access_token usando o refresh_token")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        log.info("[AUTH-CONTROLLER] Refresh token request");
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout", description = "Invalida a sessao no Keycloak")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        log.info("[AUTH-CONTROLLER] Logout request");
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Dados do usuario autenticado", description = "Retorna informacoes extraidas do JWT")
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new LinkedHashMap<>();
        userInfo.put("sub", jwt.getSubject());
        userInfo.put("username", jwt.getClaimAsString("preferred_username"));
        userInfo.put("email", jwt.getClaimAsString("email"));
        userInfo.put("name", jwt.getClaimAsString("name"));
        userInfo.put("realm_access", jwt.getClaimAsMap("realm_access"));
        userInfo.put("issued_at", jwt.getIssuedAt());
        userInfo.put("expires_at", jwt.getExpiresAt());

        return ResponseEntity.ok(userInfo);
    }
}