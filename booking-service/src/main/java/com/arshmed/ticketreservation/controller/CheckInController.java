package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.request.CheckInRequest;
import com.arshmed.ticketreservation.dto.response.CheckInResponse;
import com.arshmed.ticketreservation.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(CHECKIN)
public class CheckInController {

    private final CheckInService checkInService;

    @GetMapping("/{booking-number}")
    public ResponseEntity<CheckInResponse> getCheckIn(
            @PathVariable(name = "booking-number") String bookingNumber) {
        return ResponseEntity.ok(checkInService.getCheckIn(bookingNumber));
    }

    @PostMapping
    public ResponseEntity<CheckInResponse> checkIn(@RequestBody CheckInRequest request) {
        return ResponseEntity.ok(checkInService.checkIn(request));
    }

    @PutMapping("/{booking-number}")
    public ResponseEntity<CheckInResponse> updateCheckIn(
            @PathVariable(name = "booking-number") String bookingNumber,
            @RequestBody CheckInRequest request) {
        return ResponseEntity.ok(checkInService.updateCheckIn(bookingNumber, request));
    }
}
