package com.arshmed.ticketreservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AirportRequest(
        String id,
        @NotNull(message = "Airport code is required")
        @Pattern(
                regexp = "^[A-Z]{3}$|^[A-Z]{4}$",
                message = "Airport code must be either 3 or 4 uppercase letters (IATA or ICAO)."
        )
        String airportCode,
        @NotNull(message = "Airport name is required")
        @Size(min = 5, max = 50, message = "Airport name length must be between 5 and 50 characters length")
        String airportName,
        @NotNull(message = "Airport country is required")
        String country,
        @NotNull(message = "Airport city is required")
        String city,
        String state
) {
}
