package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.request.FlightRequest;
import com.arshmed.ticketreservation.dto.response.FlightResponse;
import com.arshmed.ticketreservation.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(FLIGHTS)
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@RequestBody @Valid FlightRequest request) {
        return ResponseEntity.ok(flightService.createFlight(request));
    }

    @GetMapping(AIRCRAFT_FLIGHTS + "/{tail-number}")
    public ResponseEntity<List<FlightResponse>> findAircraftFlights(@PathVariable(name = "tail-number") String tailNumber) {
        return ResponseEntity.ok(flightService.findAircraftFlights(tailNumber));
    }

    @GetMapping(AIRPORT_FLIGHTS + "/{airport-code}")
    public ResponseEntity<List<FlightResponse>> findAirportFlights(@PathVariable(name = "airport-code") String airportCode) {
        return ResponseEntity.ok(flightService.findAirportFlights(airportCode));
    }

    @GetMapping
    public ResponseEntity<List<FlightResponse>> findAll() {
        return ResponseEntity.ok(flightService.findAll());
    }

    @GetMapping("/{flight-id}")
    public ResponseEntity<FlightResponse> findById(@PathVariable(name = "flight-id") String id) {
        return ResponseEntity.ok(flightService.findById(id));
    }

}
