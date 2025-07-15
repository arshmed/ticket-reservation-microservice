package com.arshmed.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterRequest(
        @NotEmpty(message = "Firstname cannot be empty")
        String firstname,
        @NotEmpty(message = "Lastname cannot be empty")
        String lastname,
        @NotEmpty(message = "Password cannot be empty")
        String password,
        @Email(message = "Email format is not valid")
        @NotEmpty(message = "Email cannot be empty")
        String email
) {}