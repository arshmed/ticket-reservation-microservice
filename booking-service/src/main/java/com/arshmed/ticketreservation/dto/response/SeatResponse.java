package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;

public record SeatResponse(
        String id,
        String flightId,
        String seatNumber,
        String seatRow,
        String seatColumn,
        String seatClass,
        boolean windowSeat,
        boolean aisleSeat,
        boolean extraLegroom,
        boolean exitRow,
        BigDecimal price,
        String status
) implements Serializable {}
