package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.response.SeatResponse;
import com.arshmed.ticketreservation.entity.Seat;
import com.arshmed.ticketreservation.service.FlightService;
import com.arshmed.ticketreservation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(SEATS)
public class SeatController {

    private final SeatService seatService;
    private final FlightService flightService;

    @PostMapping("/generate/{flight-id}")
    public ResponseEntity<List<SeatResponse>> generateSeats(
            @PathVariable(name = "flight-id") String flightId) {
        var flight = flightService.findFlightEntity(flightId);
        List<Seat> seats = seatService.generateSeatsForFlight(flight);
        return ResponseEntity.ok(seats.stream().map(this::toResponse).toList());
    }

    @GetMapping(FLIGHT_SEATS + "/{flight-id}")
    public ResponseEntity<List<SeatResponse>> getFlightSeats(
            @PathVariable(name = "flight-id") String flightId) {
        List<Seat> seats = seatService.getFlightSeats(flightId);
        return ResponseEntity.ok(seats.stream().map(this::toResponse).toList());
    }

    @GetMapping(FLIGHT_SEATS + "/{flight-id}/{seat-class}")
    public ResponseEntity<List<SeatResponse>> getFlightSeatsByClass(
            @PathVariable(name = "flight-id") String flightId,
            @PathVariable(name = "seat-class") String seatClass) {
        List<Seat> seats = seatService.getFlightSeatsByClass(flightId, seatClass);
        return ResponseEntity.ok(seats.stream().map(this::toResponse).toList());
    }

    private SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(), seat.getFlightId(), seat.getSeatNumber(),
                seat.getSeatRow(), seat.getSeatColumn(), seat.getSeatClass(),
                seat.isWindowSeat(), seat.isAisleSeat(), seat.isExtraLegroom(),
                seat.isExitRow(), seat.getPrice(), seat.getStatus());
    }
}
