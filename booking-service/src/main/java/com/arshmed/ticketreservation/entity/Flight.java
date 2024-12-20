package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Flight {

    @Id
    private String id;
    private String flightNumber;
    private String aircraftTailNumber;
    private String departureAirportCode;
    private String destinationAirportCode;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal flightCharge;

}
