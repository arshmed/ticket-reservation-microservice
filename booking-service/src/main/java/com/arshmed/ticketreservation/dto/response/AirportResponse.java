package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;

public record AirportResponse(
        String id,
        String airportCode,
        String airportName,
        String country,
        String city,
        String state
) implements Serializable {}