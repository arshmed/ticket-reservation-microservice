package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CalendarPriceResponse(
        LocalDate date,
        String departureAirportCode,
        String destinationAirportCode,
        BigDecimal cheapestPrice,
        String seatClass,
        int flightCount,
        String cheapestFlightNumber
) implements Serializable {}
