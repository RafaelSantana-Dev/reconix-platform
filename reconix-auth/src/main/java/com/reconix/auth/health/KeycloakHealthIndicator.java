package com.reconix.auth.health;

import com.reconix.auth.config.KeycloakProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KeycloakHealthIndicator implements HealthIndicator {

    private final KeycloakProperties keycloakProperties;
    private final RestClient restClient;

    public KeycloakHealthIndicator(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        this.restClient = RestClient.builder().build();
    }

    @Override
    public Health health() {
        try {
            String realmUrl = keycloakProperties.serverUrl()
                    + "/realms/" + keycloakProperties.realm();

            restClient.get()
                    .uri(realmUrl)
                    .retrieve()
                    .toBodilessEntity();

            return Health.up()
                    .withDetail("keycloak_url", keycloakProperties.serverUrl())
                    .withDetail("realm", keycloakProperties.realm())
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("keycloak_url", keycloakProperties.serverUrl())
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}