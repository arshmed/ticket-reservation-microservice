package com.arshmed.authservice.dto;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        Role role,
        String email,
        String phoneNumber
) {
}
