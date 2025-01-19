package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends MongoRepository<Flight, String> {
    Page<Flight> findAllByDepartureTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Flight> findAllByDepartureTimeBetweenAndDepartureAirportCodeAndDestinationAirportCode(
            LocalDateTime start, LocalDateTime end, String departureAirportCode, String destinationAirportCode, Pageable pageable);
    Optional<Flight> findOptionalByFlightNumber(String flightNumber);
    List<Flight> findAllByAircraftTailNumber(String aircraftTailNumber);
    List<Flight> findAllByDepartureAirportCode(String airportCode);
}
