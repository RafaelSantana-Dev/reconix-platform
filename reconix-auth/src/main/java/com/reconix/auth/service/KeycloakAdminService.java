package com.reconix.auth.service;

import com.reconix.auth.config.KeycloakProperties;
import com.reconix.auth.dto.CreateUserRequest;
import com.reconix.auth.dto.UserResponse;
import com.reconix.auth.exception.AuthenticationFailedException;
import com.reconix.auth.exception.UserAlreadyExistsException;
import com.reconix.auth.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminService.class);

    private final RestClient keycloakAdminRestClient;
    private final RestClient keycloakRestClient;
    private final KeycloakProperties keycloakProperties;

    public KeycloakAdminService(
            RestClient keycloakAdminRestClient,
            RestClient keycloakRestClient,
            KeycloakProperties keycloakProperties
    ) {
        this.keycloakAdminRestClient = keycloakAdminRestClient;
        this.keycloakRestClient = keycloakRestClient;
        this.keycloakProperties = keycloakProperties;
    }

    /**
     * Obtem token de admin via master realm para chamar a Keycloak Admin API.
     */
    @SuppressWarnings("unchecked")
    private String getAdminToken() {
        String formBody = "grant_type=password"
                + "&client_id=admin-cli"
                + "&username=" + keycloakProperties.adminUsername()
                + "&password=" + keycloakProperties.adminPassword();

        try {
            Map<String, Object> response = keycloakRestClient.post()
                    .uri(keycloakProperties.adminTokenUrl())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formBody)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                throw new AuthenticationFailedException("Resposta vazia do Keycloak Admin");
            }

            return (String) response.get("access_token");

        } catch (RestClientResponseException e) {
            log.error("[AUTH-ADMIN] Falha ao obter admin token: {}", e.getStatusCode());
            throw new AuthenticationFailedException("Falha ao autenticar com Keycloak Admin API");
        }
    }

    /**
     * Lista todos os usuarios do realm.
     */
    @SuppressWarnings("unchecked")
    public List<UserResponse> listUsers() {
        String adminToken = getAdminToken();

        List<Map<String, Object>> users = keycloakAdminRestClient.get()
                .uri(keycloakProperties.usersUrl())
                .header("Authorization", "Bearer " + adminToken)
                .retrieve()
                .body(List.class);

        if (users == null) return Collections.emptyList();

        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    /**
     * Busca um usuario por ID.
     */
    @SuppressWarnings("unchecked")
    public UserResponse getUserById(String userId) {
        String adminToken = getAdminToken();

        try {
            Map<String, Object> user = keycloakAdminRestClient.get()
                    .uri(keycloakProperties.userUrl(userId))
                    .header("Authorization", "Bearer " + adminToken)
                    .retrieve()
                    .body(Map.class);

            if (user == null) throw new UserNotFoundException(userId);

            List<String> roles = getUserRoles(userId, adminToken);
            return mapToUserResponse(user, roles);

        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(userId);
            }
            throw e;
        }
    }

    /**
     * Cria um novo usuario no Keycloak.
     */
    public UserResponse createUser(CreateUserRequest request) {
        String adminToken = getAdminToken();

        Map<String, Object> userRepresentation = new LinkedHashMap<>();
        userRepresentation.put("username", request.username());
        userRepresentation.put("email", request.email());
        userRepresentation.put("firstName", request.firstName());
        userRepresentation.put("lastName", request.lastName());
        userRepresentation.put("enabled", true);
        userRepresentation.put("emailVerified", true);

        Map<String, Object> credential = new LinkedHashMap<>();
        credential.put("type", "password");
        credential.put("value", request.password());
        credential.put("temporary", false);
        userRepresentation.put("credentials", List.of(credential));

        try {
            var responseEntity = keycloakAdminRestClient.post()
                    .uri(keycloakProperties.usersUrl())
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userRepresentation)
                    .retrieve()
                    .toBodilessEntity();

            String location = responseEntity.getHeaders().getFirst("Location");
            String userId = location != null ? location.substring(location.lastIndexOf("/") + 1) : null;

            log.info("[AUTH-ADMIN] Usuario criado com sucesso: {} (ID: {})", request.username(), userId);

            if (request.roles() != null && !request.roles().isEmpty() && userId != null) {
                assignRoles(userId, request.roles(), adminToken);
            }

            if (userId != null) {
                return getUserById(userId);
            }

            return new UserResponse(userId, request.username(), request.email(),
                    request.firstName(), request.lastName(), true,
                    request.roles() != null ? request.roles() : Collections.emptyList());

        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new UserAlreadyExistsException(request.username());
            }
            log.error("[AUTH-ADMIN] Erro ao criar usuario: {} | Status: {}",
                    request.username(), e.getStatusCode());
            throw e;
        }
    }

    /**
     * Deleta um usuario pelo ID.
     */
    public void deleteUser(String userId) {
        String adminToken = getAdminToken();

        try {
            keycloakAdminRestClient.delete()
                    .uri(keycloakProperties.userUrl(userId))
                    .header("Authorization", "Bearer " + adminToken)
                    .retrieve()
                    .toBodilessEntity();

            log.info("[AUTH-ADMIN] Usuario deletado: {}", userId);

        } catch (RestClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(userId);
            }
            throw e;
        }
    }

    /**
     * Busca os roles de um usuario.
     */
    @SuppressWarnings("unchecked")
    private List<String> getUserRoles(String userId, String adminToken) {
        try {
            List<Map<String, Object>> roles = keycloakAdminRestClient.get()
                    .uri(keycloakProperties.userRoleMappingsUrl(userId))
                    .header("Authorization", "Bearer " + adminToken)
                    .retrieve()
                    .body(List.class);

            if (roles == null) return Collections.emptyList();

            return roles.stream()
                    .map(r -> (String) r.get("name"))
                    .filter(name -> name.startsWith("ROLE_"))
                    .toList();

        } catch (Exception e) {
            log.warn("[AUTH-ADMIN] Falha ao buscar roles do usuario: {}", userId);
            return Collections.emptyList();
        }
    }

    /**
     * Atribui realm roles a um usuario.
     */
    @SuppressWarnings("unchecked")
    private void assignRoles(String userId, List<String> roleNames, String adminToken) {
        List<Map<String, Object>> allRoles = keycloakAdminRestClient.get()
                .uri(keycloakProperties.realmRolesUrl())
                .header("Authorization", "Bearer " + adminToken)
                .retrieve()
                .body(List.class);

        if (allRoles == null) return;

        List<Map<String, Object>> rolesToAssign = allRoles.stream()
                .filter(r -> roleNames.contains((String) r.get("name")))
                .toList();

        if (rolesToAssign.isEmpty()) return;

        keycloakAdminRestClient.post()
                .uri(keycloakProperties.userRoleMappingsUrl(userId))
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(rolesToAssign)
                .retrieve()
                .toBodilessEntity();

        log.info("[AUTH-ADMIN] Roles atribuidas ao usuario {}: {}", userId, roleNames);
    }

    private UserResponse mapToUserResponse(Map<String, Object> user) {
        return mapToUserResponse(user, Collections.emptyList());
    }

    private UserResponse mapToUserResponse(Map<String, Object> user, List<String> roles) {
        return new UserResponse(
                (String) user.get("id"),
                (String) user.get("username"),
                (String) user.get("email"),
                (String) user.get("firstName"),
                (String) user.get("lastName"),
                Boolean.TRUE.equals(user.get("enabled")),
                roles
        );
    }
}