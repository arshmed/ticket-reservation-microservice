package com.arshmed.ticketreservation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record CheckInResponse(
        String id,
        String bookingNumber,
        String customerId,
        String flightId,
        String status,
        List<PassengerCheckInResponse> passengers,
        LocalDateTime checkedInAt,
        LocalDateTime createdAt
) {
    public record PassengerCheckInResponse(
            String firstName,
            String lastName,
            String seatNumber,
            String seatClass,
            String mealPreference,
            Boolean seatChanged
    ) {}
}
