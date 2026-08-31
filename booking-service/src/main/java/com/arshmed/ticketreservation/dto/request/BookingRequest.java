package com.arshmed.ticketreservation.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record BookingRequest(
        String flightId,
        String customerId,
        List<PassengerRequest> passengers,
        List<String> ancillaryServiceCodes,
        Long milesToRedeem,
        String roundTripGroupId,
        String linkedBookingId,
        String returnFlightId,
        List<PassengerRequest> returnPassengers
) {
    public record PassengerRequest(
            String firstName,
            String lastName,
            String dateOfBirth,
            String passengerType,
            String discountType,
            String seatClass,
            String seatNumber,
            BigDecimal fareAmount
    ) {}
}
