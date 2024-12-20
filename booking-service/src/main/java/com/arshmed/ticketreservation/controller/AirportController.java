package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.request.AirportRequest;
import com.arshmed.ticketreservation.dto.response.AirportResponse;
import com.arshmed.ticketreservation.service.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(AIRPORTS)
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    public ResponseEntity<String> createAirport(@RequestBody @Valid AirportRequest request) {
        return ResponseEntity.ok(airportService.createAirport(request));
    }

    @GetMapping
    public ResponseEntity<List<AirportResponse>> findAll() {
        return ResponseEntity.ok(airportService.findAll());
    }

    @GetMapping("/{airport-id}")
    public ResponseEntity<AirportResponse> findById(@PathVariable(name = "airport-id") String id) {
        return ResponseEntity.ok(airportService.findById(id));
    }

    @GetMapping(AIRPORT_CODE + "/{airport-code}")
    public ResponseEntity<AirportResponse> findByAirportCode(@PathVariable(name = "airport-code") String code) {
        return ResponseEntity.ok(airportService.findByAirportCode(code));
    }

}
