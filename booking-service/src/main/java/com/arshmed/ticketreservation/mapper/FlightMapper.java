package com.arshmed.ticketreservation.mapper;

import com.arshmed.ticketreservation.dto.response.AircraftResponse;
import com.arshmed.ticketreservation.dto.response.AirportResponse;
import com.arshmed.ticketreservation.dto.request.FlightRequest;
import com.arshmed.ticketreservation.dto.response.FlightResponse;
import com.arshmed.ticketreservation.entity.Flight;
import com.arshmed.ticketreservation.service.AircraftService;
import com.arshmed.ticketreservation.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class FlightMapper {

    private final AircraftService aircraftService;
    private final AirportService airportService;

    public FlightResponse fromFlight(Flight flight) {

        AircraftResponse aircraft = aircraftService.findByTailNumber(flight.getAircraftTailNumber());

        String aircraftManufacturer = aircraft.manufacturer();
        String aircraftModel = aircraft.model();

        AirportResponse departureAirport = airportService.findByAirportCode(flight.getDepartureAirportCode());
        AirportResponse destinationAirport = airportService.findByAirportCode(flight.getDestinationAirportCode());

        String departureAirportName = departureAirport.airportName();
        String destinationAirportName = destinationAirport.airportName();

        return new FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getAircraftTailNumber(),
                aircraftManufacturer,
                aircraftModel,
                flight.getDepartureAirportCode(),
                departureAirportName,
                flight.getDestinationAirportCode(),
                destinationAirportName,
                departureAirport.city(),
                destinationAirport.city(),
                flight.getDepartureDate(),
                flight.getArrivalDate(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getFlightCharge()
        );
    }

    public Flight toFlight(FlightRequest request) {
        return Flight.builder()
                .id(request.id())
                .flightNumber(request.flightNumber())
                .aircraftTailNumber(request.aircraftTailNumber())
                .departureAirportCode(request.departureAirportCode())
                .destinationAirportCode(request.destinationAirportCode())
                .departureTime(request.departureTime())
                .arrivalTime(request.arrivalTime())
                .flightCharge(request.flightCharge())
                .build();
    }
}
