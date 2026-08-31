package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.response.CalendarPriceResponse;
import com.arshmed.ticketreservation.entity.Flight;
import com.arshmed.ticketreservation.entity.Seat;
import com.arshmed.ticketreservation.repository.FlightRepository;
import com.arshmed.ticketreservation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final PricingService pricingService;

    public List<CalendarPriceResponse> getCalendarPrices(
            String departureAirportCode,
            String destinationAirportCode,
            LocalDate startDate,
            LocalDate endDate
    ) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Flight> flights = flightRepository
                .findAllByDepartureTimeBetweenAndDepartureAirportCodeAndDestinationAirportCode(
                        startDateTime, endDateTime, departureAirportCode, destinationAirportCode,
                        org.springframework.data.domain.PageRequest.of(0, 100)
                ).getContent();

        if (flights.isEmpty()) return List.of();

        List<String> flightIds = flights.stream().map(Flight::getId).toList();
        List<Seat> allSeats = seatRepository.findAllByFlightIdIn(flightIds);

        Map<String, Map<String, List<Seat>>> seatsByFlightAndClass = new HashMap<>();
        for (Seat seat : allSeats) {
            seatsByFlightAndClass
                    .computeIfAbsent(seat.getFlightId(), k -> new HashMap<>())
                    .computeIfAbsent(seat.getSeatClass(), k -> new ArrayList<>())
                    .add(seat);
        }

        Map<LocalDate, List<CalendarPriceResponse>> priceMap = new HashMap<>();

        for (Flight flight : flights) {
            LocalDate date = flight.getDepartureTime().toLocalDate();
            BigDecimal cheapestPrice = flight.getFlightCharge();

            Map<String, List<Seat>> classSeats = seatsByFlightAndClass.getOrDefault(flight.getId(), Map.of());

            for (String seatClass : List.of("ECONOMY", "BUSINESS", "FIRST")) {
                List<Seat> seats = classSeats.get(seatClass);
                if (seats == null || seats.isEmpty()) continue;
                int totalSeats = seats.size();
                long bookedSeats = seats.stream().filter(s -> "RESERVED".equals(s.getStatus())).count();
                BigDecimal dynamicPrice = pricingService.calculateDynamicPrice(cheapestPrice, seatClass, totalSeats, (int) bookedSeats);

                if (dynamicPrice.compareTo(cheapestPrice) < 0) {
                    cheapestPrice = dynamicPrice;
                }
            }

            priceMap.computeIfAbsent(date, k -> new ArrayList<>()).add(
                    new CalendarPriceResponse(date, flight.getDepartureAirportCode(),
                            flight.getDestinationAirportCode(), cheapestPrice,
                            "ECONOMY", 1, flight.getFlightNumber())
            );
        }

        return priceMap.entrySet().stream()
                .map(entry -> entry.getValue().stream()
                        .min(Comparator.comparing(CalendarPriceResponse::cheapestPrice))
                        .orElse(null))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(CalendarPriceResponse::date))
                .toList();
    }
}
