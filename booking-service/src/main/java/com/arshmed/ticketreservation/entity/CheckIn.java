package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class CheckIn {
    @Id
    private String id;
    private String bookingNumber;
    private String customerId;
    private String flightId;
    private String status;
    private List<PassengerCheckIn> passengers;
    private LocalDateTime checkedInAt;
    private LocalDateTime createdAt;
}
