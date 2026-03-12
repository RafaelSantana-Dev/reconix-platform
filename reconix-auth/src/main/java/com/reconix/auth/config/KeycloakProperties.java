package com.reconix.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String serverUrl,
        String realm,
        String clientId,
        String clientSecret,
        String adminUsername,
        String adminPassword
) {

    public String tokenUrl() {
        return serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
    }

    public String logoutUrl() {
        return serverUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
    }

    public String usersUrl() {
        return serverUrl + "/admin/realms/" + realm + "/users";
    }

    public String userUrl(String userId) {
        return usersUrl() + "/" + userId;
    }

    public String userRoleMappingsUrl(String userId) {
        return userUrl(userId) + "/role-mappings/realm";
    }

    public String realmRolesUrl() {
        return serverUrl + "/admin/realms/" + realm + "/roles";
    }

    public String adminTokenUrl() {
        return serverUrl + "/realms/master/protocol/openid-connect/token";
    }
}