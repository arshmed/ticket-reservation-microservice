package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.MilesTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MilesTransactionRepository extends MongoRepository<MilesTransaction, String> {
    List<MilesTransaction> findAllByCustomerIdOrderByCreatedAtDesc(String customerId);
}
