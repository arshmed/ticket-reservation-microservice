package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class MilesTransaction {
    @Id
    private String id;
    private String customerId;
    private String bookingNumber;
    private String transactionType;
    private long miles;
    private String description;
    private LocalDateTime createdAt;
}
