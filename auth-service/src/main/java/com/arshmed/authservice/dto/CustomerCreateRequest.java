package com.arshmed.authservice.dto;

public record CustomerCreateRequest(
        String firstname,
        String lastname,
        String password,
        String email,
        String phoneNumber
) {
}
