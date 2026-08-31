package com.arshmed.ticketreservation.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PassengerCheckIn {
    private String firstName;
    private String lastName;
    private String seatNumber;
    private String seatClass;
    private String mealPreference;
    private Boolean seatChanged;
}
