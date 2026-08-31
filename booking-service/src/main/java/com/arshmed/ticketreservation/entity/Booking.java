package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Booking {
    @Id
    private String id;
    private String bookingNumber;
    private String customerId;
    private String flightId;
    private String flightNumber;
    private String departureAirportCode;
    private String destinationAirportCode;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private List<BookingPassenger> passengers;
    private BigDecimal flightCharge;
    private BigDecimal discountTotal;
    private BigDecimal ancillaryTotal;
    private BigDecimal milesDiscount;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private String status;
    private long milesEarned;
    private LocalDateTime createdAt;
    private String roundTripBookingId;
    private String roundTripGroupId;
}
