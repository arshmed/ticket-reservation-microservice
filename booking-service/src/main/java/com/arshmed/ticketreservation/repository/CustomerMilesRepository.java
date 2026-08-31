package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.CustomerMiles;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CustomerMilesRepository extends MongoRepository<CustomerMiles, String> {
    Optional<CustomerMiles> findByCustomerId(String customerId);
}
