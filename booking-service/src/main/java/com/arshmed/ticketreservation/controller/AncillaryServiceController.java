package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.entity.AncillaryService;
import com.arshmed.ticketreservation.service.AncillaryServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ANCILLARY)
public class AncillaryServiceController {

    private final AncillaryServiceService service;

    @GetMapping
    public ResponseEntity<List<AncillaryService>> getAll() {
        return ResponseEntity.ok(service.getAllActive());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<AncillaryService>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    @PostMapping
    public ResponseEntity<AncillaryService> create(@RequestBody AncillaryService anc) {
        return ResponseEntity.ok(service.create(anc));
    }
}
