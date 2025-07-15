package com.arshmed.authservice.dto;

import com.arshmed.authservice.entity.Role;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        Role role,
        String email,
        String phoneNumber
) {
}
