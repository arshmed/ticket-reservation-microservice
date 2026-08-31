package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.entity.AncillaryService;
import com.arshmed.ticketreservation.repository.AncillaryServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AncillaryServiceService {

    private final AncillaryServiceRepository repository;

    public List<AncillaryService> getAllActive() {
        return repository.findAllByActiveTrue();
    }

    public List<AncillaryService> getByCategory(String category) {
        return repository.findAllByCategoryAndActiveTrue(category);
    }

    public AncillaryService getByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Service not found: " + code));
    }

    public AncillaryService create(AncillaryService service) {
        return repository.save(service);
    }
}
