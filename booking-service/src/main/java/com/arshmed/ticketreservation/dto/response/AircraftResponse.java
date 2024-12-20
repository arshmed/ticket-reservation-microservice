package com.arshmed.ticketreservation.dto.response;

public record AircraftResponse(
        String id,
        String tailNumber,
        String manufacturer,
        String model,
        Integer seatNumber
) {
}
