package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.CheckIn;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CheckInRepository extends MongoRepository<CheckIn, String> {
    Optional<CheckIn> findByBookingNumber(String bookingNumber);
    boolean existsByBookingNumber(String bookingNumber);
}
