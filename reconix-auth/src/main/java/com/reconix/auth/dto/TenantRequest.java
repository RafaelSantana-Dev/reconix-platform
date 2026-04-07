package com.reconix.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TenantRequest(
        @NotBlank(message = "Nome do tenant é obrigatório")
        @Size(min = 3, max = 100)
        String name,

        @NotBlank(message = "Slug é obrigatório")
        @Size(min = 3, max = 50)
        String slug
) {}