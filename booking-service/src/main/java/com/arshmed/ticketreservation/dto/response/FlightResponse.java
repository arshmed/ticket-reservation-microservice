package com.arshmed.ticketreservation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FlightResponse(
        String flightId,
        String flightNumber,
        String aircraftTailNumber,
        String aircraftManufacturer,
        String aircraftModel,
        String departureAirportCode,
        String departureAirportName,
        String destinationAirportCode,
        String destinationAirportName,
        String departureCity,
        String arrivalCity,
        LocalDate departureDate,
        LocalDate arrivalDate,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        BigDecimal flightCharge
) {
}
