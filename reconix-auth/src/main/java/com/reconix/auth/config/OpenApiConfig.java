package com.reconix.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reconixAuthOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reconix Auth Service API")
                        .description("Servico de autenticacao e gerenciamento de usuarios do Reconix Platform. "
                                + "Integrado com Keycloak (OAuth2/OIDC) para autenticacao segura.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Reconix Team")
                                .email("contato@reconix.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer JWT"))
                .components(new Components()
                        .addSecuritySchemes("Bearer JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o access_token JWT obtido no endpoint /api/v1/auth/login")));
    }
}