package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.Aircraft;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AircraftRepository extends MongoRepository<Aircraft, String> {
    Optional<Aircraft> findByTailNumber(String tailNumber);
}
