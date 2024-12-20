package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.request.AircraftRequest;
import com.arshmed.ticketreservation.dto.response.AircraftResponse;
import com.arshmed.ticketreservation.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(AIRCRAFT)
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping
    public ResponseEntity<String> createAircraft(
            @RequestBody @Valid AircraftRequest request
    ) {
        return ResponseEntity.ok(aircraftService.createAircraft(request));
    }

    @GetMapping("/{aircraft-id}")
    public ResponseEntity<AircraftResponse> findById(@PathVariable(name = "aircraft-id") String id) {
        return ResponseEntity.ok(aircraftService.findById(id));
    }

    @GetMapping(TAIL_NUMBER + "/{tail-number}")
    public ResponseEntity<AircraftResponse> findByTailNumber(@PathVariable(name = "tail-number") String tailNumber) {
        return ResponseEntity.ok(aircraftService.findByTailNumber(tailNumber));
    }

    @GetMapping
    public ResponseEntity<List<AircraftResponse>> findAll() {
        return ResponseEntity.ok(aircraftService.findAll());
    }

}
