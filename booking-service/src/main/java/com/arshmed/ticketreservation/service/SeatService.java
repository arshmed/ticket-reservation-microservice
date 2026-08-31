package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.entity.Flight;
import com.arshmed.ticketreservation.entity.Seat;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

import static com.arshmed.ticketreservation.exception.ErrorType.BAD_REQUEST_ERROR;
import static com.arshmed.ticketreservation.exception.ErrorType.SEAT_NOT_AVAILABLE;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final PricingService pricingService;

    public List<Seat> generateSeatsForFlight(Flight flight) {
        List<Seat> seats = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F"};
        int totalRows = 30;

        for (int row = 1; row <= totalRows; row++) {
            for (int col = 0; col < rows.length; col++) {
                String seatClass;
                BigDecimal price;

                if (row <= 3) {
                    seatClass = "FIRST";
                    price = pricingService.calculateDynamicPrice(
                            flight.getFlightCharge(), seatClass, 18, 0);
                } else if (row <= 10) {
                    seatClass = "BUSINESS";
                    price = pricingService.calculateDynamicPrice(
                            flight.getFlightCharge(), seatClass, 42, 0);
                } else {
                    seatClass = "ECONOMY";
                    price = pricingService.calculateDynamicPrice(
                            flight.getFlightCharge(), seatClass, 120, 0);
                }

                String seatNumber = row + rows[col];
                seats.add(Seat.builder()
                        .flightId(flight.getId())
                        .seatNumber(seatNumber)
                        .seatRow(String.valueOf(row))
                        .seatColumn(rows[col])
                        .seatClass(seatClass)
                        .windowSeat(col == 0 || col == rows.length - 1)
                        .aisleSeat(col == 2 || col == 3)
                        .extraLegroom(row == 1 || row == 11)
                        .exitRow(row == 11 || row == 22)
                        .price(price)
                        .status("AVAILABLE")
                        .build());
            }
        }

        return seatRepository.saveAll(seats);
    }

    public List<Seat> getFlightSeats(String flightId) {
        return seatRepository.findAllByFlightId(flightId);
    }

    public List<Seat> getFlightSeatsByClass(String flightId, String seatClass) {
        return seatRepository.findAllByFlightIdAndSeatClass(flightId, seatClass);
    }

    public Seat selectSeat(String flightId, String seatNumber) {
        Seat seat = seatRepository.findByFlightIdAndSeatNumber(flightId, seatNumber)
                .orElseThrow(() -> new BookingException(BAD_REQUEST_ERROR));

        if (!seat.getStatus().equals("AVAILABLE")) {
            throw new BookingException(BAD_REQUEST_ERROR);
        }

        seat.setStatus("RESERVED");
        return seatRepository.save(seat);
    }

    public void releaseSeat(String flightId, String seatNumber) {
        seatRepository.findByFlightIdAndSeatNumber(flightId, seatNumber)
                .ifPresent(seat -> {
                    seat.setStatus("AVAILABLE");
                    seatRepository.save(seat);
                });
    }

    public Seat autoAssignSeat(String flightId, String seatClass) {
        List<Seat> available = seatRepository.findAllByFlightIdAndSeatClass(flightId, seatClass)
                .stream()
                .filter(s -> s.getStatus().equals("AVAILABLE"))
                .toList();
        if (available.isEmpty()) {
            throw new BookingException(SEAT_NOT_AVAILABLE);
        }
        Seat seat = available.get(0);
        seat.setStatus("RESERVED");
        return seatRepository.save(seat);
    }

    public long getBookedSeatCount(String flightId) {
        return seatRepository.countByFlightIdAndStatus(flightId, "RESERVED");
    }

    public long getBookedSeatCountByClass(String flightId, String seatClass) {
        return seatRepository.countByFlightIdAndSeatClassAndStatus(flightId, seatClass, "RESERVED");
    }

    public int getTotalSeatsByClass(String flightId, String seatClass) {
        return seatRepository.findAllByFlightIdAndSeatClass(flightId, seatClass).size();
    }

    public Map<String, Map<String, Long>> getSeatStatsByFlight(String flightId) {
        List<Seat> allSeats = seatRepository.findAllByFlightId(flightId);
        Map<String, Map<String, Long>> stats = new LinkedHashMap<>();
        for (String seatClass : List.of("ECONOMY", "BUSINESS", "FIRST")) {
            long total = allSeats.stream().filter(s -> s.getSeatClass().equals(seatClass)).count();
            if (total == 0) {
                total = seatClass.equals("ECONOMY") ? 120 : seatClass.equals("BUSINESS") ? 42 : 18;
            }
            long booked = allSeats.stream()
                    .filter(s -> s.getSeatClass().equals(seatClass) && "RESERVED".equals(s.getStatus()))
                    .count();
            Map<String, Long> classStats = new LinkedHashMap<>();
            classStats.put("total", total);
            classStats.put("booked", booked);
            stats.put(seatClass, classStats);
        }
        return stats;
    }
}
