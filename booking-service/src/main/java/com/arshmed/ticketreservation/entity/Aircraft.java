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
public class Aircraft {

    @Id
    private String id;
    @Indexed(unique = true)
    private String tailNumber;
    private String manufacturer;
    private String model;
    private Integer numberOfSeats;

}
