package com.arshmed.authservice.dto;

public record CustomerLoginRequest(
        String email,
        String password
) {
}
