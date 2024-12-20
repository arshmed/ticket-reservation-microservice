package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AirportRepository extends MongoRepository<Airport, String> {
    Optional<Airport> findByAirportCode(String code);
}
