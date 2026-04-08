package com.reconix.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakConfig {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Bean
    public RestClient keycloakRestClient() {
        return RestClient.builder()
                .baseUrl(serverUrl + "/realms/" + realm)
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }

    @Bean
    public RestClient keycloakAdminRestClient() {
        return RestClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}