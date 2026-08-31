package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class CustomerMiles {
    @Id
    private String id;
    @Indexed(unique = true)
    private String customerId;
    private long totalEarned;
    private long available;
    private long redeemed;
    private LocalDateTime lastUpdated;
}
