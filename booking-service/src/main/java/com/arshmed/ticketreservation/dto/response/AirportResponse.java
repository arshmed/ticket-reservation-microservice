package com.arshmed.ticketreservation.dto.response;

public record AirportResponse(
        String id,
        String airportCode,
        String airportName,
        String country,
        String city,
        String state
) {
}
