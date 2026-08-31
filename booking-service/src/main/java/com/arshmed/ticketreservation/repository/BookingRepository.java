package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findAllByCustomerIdOrderByCreatedAtDesc(String customerId);
    Optional<Booking> findByBookingNumber(String bookingNumber);
    long countByCustomerId(String customerId);
    List<Booking> findAllByRoundTripGroupId(String roundTripGroupId);
}
