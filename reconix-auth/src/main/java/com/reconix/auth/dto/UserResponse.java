package com.reconix.auth.dto;

import java.util.List;

public record UserResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean enabled,
        List<String> roles
) {}