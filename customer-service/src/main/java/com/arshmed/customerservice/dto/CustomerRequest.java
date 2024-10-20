package com.arshmed.customerservice.dto;

import com.arshmed.customerservice.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,
        @NotNull(message = "Customer firstname required")
        String firstname,
        @NotNull(message = "Customer lastname required")
        String lastname,
        @NotNull(message = "Customer email is required")
        @Email(message = "Customer Email is not a valid email address")
        String email,
        @NotNull(message = "Customer phone number is required")
        String phoneNumber,
        Address address
) {
}
