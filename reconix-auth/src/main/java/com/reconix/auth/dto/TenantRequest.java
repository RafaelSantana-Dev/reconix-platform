package com.reconix.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TenantRequest(
        @NotBlank(message = "Nome do tenant Ã© obrigatÃ³rio")
        @Size(min = 3, max = 100)
        String name,

        @NotBlank(message = "Slug Ã© obrigatÃ³rio")
        @Size(min = 3, max = 50)
        String slug
) {}