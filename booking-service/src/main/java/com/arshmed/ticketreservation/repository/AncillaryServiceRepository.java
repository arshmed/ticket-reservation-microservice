package com.arshmed.ticketreservation.repository;

import com.arshmed.ticketreservation.entity.AncillaryService;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface AncillaryServiceRepository extends MongoRepository<AncillaryService, String> {
    Optional<AncillaryService> findByCode(String code);
    List<AncillaryService> findAllByActiveTrue();
    List<AncillaryService> findAllByCategoryAndActiveTrue(String category);
}
