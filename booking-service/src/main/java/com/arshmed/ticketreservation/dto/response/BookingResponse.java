package com.arshmed.ticketreservation.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
        String id,
        String bookingNumber,
        String customerId,
        String flightId,
        String flightNumber,
        String departureAirportCode,
        String destinationAirportCode,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        List<PassengerResponse> passengers,
        BigDecimal flightCharge,
        BigDecimal discountTotal,
        BigDecimal ancillaryTotal,
        BigDecimal milesDiscount,
        BigDecimal taxAmount,
        BigDecimal grandTotal,
        String status,
        long milesEarned,
        LocalDateTime createdAt,
        String roundTripBookingId,
        String roundTripGroupId
) implements Serializable {
    public record PassengerResponse(
            String firstName,
            String lastName,
            String passengerType,
            String discountType,
            String seatClass,
            String seatNumber,
            BigDecimal baseFare,
            BigDecimal discountAmount,
            BigDecimal finalFare,
            List<String> ancillaryServiceCodes
    ) implements Serializable {}
}
