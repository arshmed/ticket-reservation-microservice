package com.arshmed.ticketreservation.dto.request;

public record CheckInRequest(
        String bookingNumber,
        java.util.List<PassengerCheckInRequest> passengers
) {
    public record PassengerCheckInRequest(
            String firstName,
            String lastName,
            String seatNumber,
            String mealPreference,
            Boolean seatChanged
    ) {}
}
