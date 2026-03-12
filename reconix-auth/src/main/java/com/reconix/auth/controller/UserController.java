package com.reconix.auth.controller;

import com.reconix.auth.dto.CreateUserRequest;
import com.reconix.auth.dto.UserResponse;
import com.reconix.auth.service.KeycloakAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/users")
@Tag(name = "Gerenciamento de Usuarios", description = "CRUD de usuarios no Keycloak (requer ROLE_ADMIN)")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final KeycloakAdminService keycloakAdminService;

    public UserController(KeycloakAdminService keycloakAdminService) {
        this.keycloakAdminService = keycloakAdminService;
    }

    @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios do realm")
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> listUsers() {
        log.info("[USER-CONTROLLER] Listando todos os usuarios");
        List<UserResponse> users = keycloakAdminService.listUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Buscar usuario por ID", description = "Retorna os dados de um usuario especifico")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        log.info("[USER-CONTROLLER] Buscando usuario: {}", id);
        UserResponse user = keycloakAdminService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Criar usuario", description = "Cria um novo usuario no Keycloak com roles")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("[USER-CONTROLLER] Criando usuario: {}", request.username());
        UserResponse user = keycloakAdminService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Deletar usuario", description = "Remove um usuario do Keycloak")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("[USER-CONTROLLER] Deletando usuario: {}", id);
        keycloakAdminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}