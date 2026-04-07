package com.reconix.auth.domain.dto;

import java.time.OffsetDateTime;

public record TenantResponse(
        Long id,
        String name,
        String slug,
        Boolean active,
        OffsetDateTime createdAt
) {}