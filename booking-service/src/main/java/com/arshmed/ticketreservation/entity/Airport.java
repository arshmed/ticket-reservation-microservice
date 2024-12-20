package com.arshmed.ticketreservation.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Airport {

    @Id
    private String id;
    @Indexed(unique = true)
    private String airportCode;
    private String airportName;
    private String country;
    private String city;
    private String state;

}
