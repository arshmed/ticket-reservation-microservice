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
public class AncillaryService {
    @Id
    private String id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private boolean active;
}
