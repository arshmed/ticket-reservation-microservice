package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public record FlightDetailResponse(
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
        BigDecimal flightCharge,
        Map<String, SeatClassInfo> seatClasses
) implements Serializable {
    public record SeatClassInfo(
            String seatClass,
            int totalSeats,
            int availableSeats,
            BigDecimal basePrice,
            BigDecimal currentPrice,
            String currency
    ) implements Serializable {}
}
