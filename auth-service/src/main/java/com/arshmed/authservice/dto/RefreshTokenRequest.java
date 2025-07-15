package com.arshmed.authservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequest(
        @NotEmpty(message = "Refresh token cannot be empty")
        String token
) {}