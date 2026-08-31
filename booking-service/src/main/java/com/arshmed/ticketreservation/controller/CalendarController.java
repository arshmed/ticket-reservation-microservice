package com.arshmed.ticketreservation.controller;

import com.arshmed.ticketreservation.dto.response.CalendarPriceResponse;
import com.arshmed.ticketreservation.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import static com.arshmed.ticketreservation.constants.RestApiList.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(CALENDAR)
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/prices")
    public ResponseEntity<List<CalendarPriceResponse>> getCalendarPrices(
            @RequestParam String departure,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                calendarService.getCalendarPrices(departure, destination, startDate, endDate));
    }
}
