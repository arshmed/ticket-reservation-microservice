package com.arshmed.ticketreservation.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record FlightRequest(
        String id,

        @NotNull(message = "Flight number is required")
        @Pattern(
                regexp = "^[A-Z]{2,3}[0-9]{2,3}$",
                message = "Flight number must be two or three letters followed by two or three digits"
        )
        String flightNumber,

        @NotNull(message = "Aircraft tail number is required")
        @Pattern(
                regexp = "^[A-Z]{2}-[0-9]{3}$",
                message = "Aircraft tail number must be in the format 'XX-123' (two letters and three digits)"
        )
        String aircraftTailNumber,

        @NotNull(message = "Departure airport information is required")
        String departureAirportCode,

        @NotNull(message = "Destination airport information is required")
        String destinationAirportCode,

        @NotNull(message = "Departure time information is required")
        @FutureOrPresent(message = "Departure time must be today or in the future")
        LocalDateTime departureTime,

        @NotNull(message = "Arrival time information is required")
        @FutureOrPresent(message = "Arrival time must be today or in the future")
        LocalDateTime arrivalTime,

        @NotNull(message = "Flight charge information is required")
        @Positive(message = "Flight charge must be positive")
        BigDecimal flightCharge
) {
}