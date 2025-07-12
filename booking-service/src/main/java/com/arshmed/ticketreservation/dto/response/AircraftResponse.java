package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;

public record AircraftResponse(
        String id,
        String tailNumber,
        String manufacturer,
        String model,
        Integer seatNumber
) implements Serializable {}