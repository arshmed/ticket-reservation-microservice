package com.arshmed.customerservice.dto;

public record CustomerLoginRequest(
        String email,
        String password
) {
}
