package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.Seat;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends MongoRepository<Seat, String> {
    List<Seat> findAllByFlightId(String flightId);
    List<Seat> findAllByFlightIdAndSeatClass(String flightId, String seatClass);
    Optional<Seat> findByFlightIdAndSeatNumber(String flightId, String seatNumber);
    long countByFlightIdAndStatus(String flightId, String status);
    long countByFlightIdAndSeatClassAndStatus(String flightId, String seatClass, String status);
    List<Seat> findAllByFlightIdIn(List<String> flightIds);
}
