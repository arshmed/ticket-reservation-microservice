package com.arshmed.customerservice.dto;

import com.arshmed.customerservice.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CustomerRequest(
        String id,
        @NotNull(message = "Customer firstname is required")
        String firstname,
        @NotNull(message = "Customer lastname is required")
        String lastname,
        @NotNull(message = "Customer password is required")
        @Pattern(
                message = "Password must be at least 8 characters, with at least one uppercase letter, one lowercase letter and a digit",
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!])(?=\\S+$).{8,}$"
        )
        String password,
        @NotNull(message = "Customer email is required")
        @Email(message = "Customer Email is not a valid email address")
        String email,
        String phoneNumber,
        @NotNull(message = "Customer Type is required")
        @Pattern(regexp = "^([IC])$", message = "Customer Type must be either 'I' or 'C'")
        String customerType,
        Address address
) {
}
