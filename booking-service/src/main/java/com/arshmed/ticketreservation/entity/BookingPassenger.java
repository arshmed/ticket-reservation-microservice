package com.arshmed.ticketreservation.entity;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BookingPassenger {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String passengerType;
    private String discountType;
    private String seatClass;
    private String seatNumber;
    private BigDecimal baseFare;
    private BigDecimal discountAmount;
    private BigDecimal finalFare;
    private List<String> ancillaryServiceCodes;
}
