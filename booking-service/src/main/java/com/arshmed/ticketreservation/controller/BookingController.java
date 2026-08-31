package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.request.BookingRequest;
import com.arshmed.ticketreservation.dto.response.BookingResponse;
import com.arshmed.ticketreservation.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(BOOKINGS)
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping(CUSTOMER_BOOKINGS + "/{customer-id}")
    public ResponseEntity<List<BookingResponse>> getCustomerBookings(
            @PathVariable(name = "customer-id") String customerId) {
        return ResponseEntity.ok(bookingService.getCustomerBookings(customerId));
    }

    @GetMapping("/{booking-number}")
    public ResponseEntity<BookingResponse> getByBookingNumber(
            @PathVariable(name = "booking-number") String bookingNumber) {
        return ResponseEntity.ok(bookingService.getByBookingNumber(bookingNumber));
    }

    @PutMapping("/{booking-number}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable(name = "booking-number") String bookingNumber) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingNumber));
    }

    @GetMapping("/roundtrip/{group-id}")
    public ResponseEntity<List<BookingResponse>> getRoundTripBookings(
            @PathVariable(name = "group-id") String groupId) {
        return ResponseEntity.ok(bookingService.getRoundTripBookings(groupId));
    }
}
