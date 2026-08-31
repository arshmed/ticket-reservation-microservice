package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Seat {
    @Id
    private String id;
    private String flightId;
    private String seatNumber;
    private String seatRow;
    private String seatColumn;
    private String seatClass;
    private boolean windowSeat;
    private boolean aisleSeat;
    private boolean extraLegroom;
    private boolean exitRow;
    private BigDecimal price;
    private String status;
}
