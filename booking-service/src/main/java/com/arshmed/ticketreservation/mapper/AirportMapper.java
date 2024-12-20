package com.arshmed.ticketreservation.mapper;

import com.arshmed.ticketreservation.dto.request.AirportRequest;
import com.arshmed.ticketreservation.dto.response.AirportResponse;
import com.arshmed.ticketreservation.entity.Airport;
import org.springframework.stereotype.Service;

@Service
public class AirportMapper {

    public AirportResponse fromAirport(Airport airport) {
        return new AirportResponse(
                airport.getId(),
                airport.getAirportCode(),
                airport.getAirportName(),
                airport.getCountry(),
                airport.getCity(),
                airport.getState()
        );
    }

    public Airport toAirport(AirportRequest request) {
        return Airport.builder()
                .id(request.id())
                .airportCode(request.airportCode())
                .airportName(request.airportName())
                .country(request.country())
                .city(request.city())
                .state(request.state())
                .build();
    }
}
