package com.arshmed.ticketreservation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AircraftRequest(
        String id,
        @NotNull(message = "Tail number is required")
        @Pattern(
                regexp = "^[A-Z]{1,2}-[0-9A-Z]{3,5}$",
                message = "Invalid tail number format. Expected format: XX-12345 or X-12345"
        )
        String tailNumber,
        @NotNull(message = "Manufacturer is required")
        String manufacturer,
        @NotNull(message = "Model is required")
        String model,
        @NotNull(message = "Number of seats is required")
        @Min(value = 2, message = "Number of seats must be at least 2")
        Integer numberOfSeats
) {
}
