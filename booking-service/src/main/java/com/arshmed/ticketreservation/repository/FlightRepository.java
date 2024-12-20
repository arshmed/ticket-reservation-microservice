package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends MongoRepository<Flight, String> {
    Optional<Flight> findOptionalByFlightNumber(String flightNumber);
    List<Flight> findAllByAircraftTailNumber(String aircraftTailNumber);
    List<Flight> findAllByDepartureAirportCode(String airportCode);
}
