package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.request.CheckInRequest;
import com.arshmed.ticketreservation.dto.response.CheckInResponse;
import com.arshmed.ticketreservation.entity.*;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.repository.BookingRepository;
import com.arshmed.ticketreservation.repository.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static com.arshmed.ticketreservation.exception.ErrorType.*;
import com.arshmed.ticketreservation.exception.ErrorType;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final BookingRepository bookingRepository;
    private final SeatService seatService;

    public CheckInResponse getCheckIn(String bookingNumber) {
        CheckIn checkIn = checkInRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new BookingException(ErrorType.CHECKIN_NOT_FOUND));
        return toResponse(checkIn);
    }

    public CheckInResponse checkIn(CheckInRequest request) {
        if (checkInRepository.existsByBookingNumber(request.bookingNumber())) {
            throw new BookingException(BAD_REQUEST_ERROR);
        }

        Booking booking = bookingRepository.findByBookingNumber(request.bookingNumber())
                .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND));

        Duration diff = Duration.between(LocalDateTime.now(), booking.getDepartureTime());
        if (diff.toHours() > 24 || diff.isNegative()) {
            throw new BookingException(BAD_REQUEST_ERROR);
        }

        List<PassengerCheckIn> passengers = new ArrayList<>();
        for (CheckInRequest.PassengerCheckInRequest pr : request.passengers()) {
            String newSeat = pr.seatNumber();
            String oldSeat = findExistingSeat(booking, pr.firstName(), pr.lastName());
            boolean seatChanged = pr.seatChanged();

            String seatNumber;
            if (newSeat == null || newSeat.isBlank()) {
                seatNumber = oldSeat;
            } else {
                seatNumber = newSeat;
                if (oldSeat != null && !oldSeat.equals(newSeat)) {
                    seatService.releaseSeat(booking.getFlightId(), oldSeat);
                    seatService.selectSeat(booking.getFlightId(), newSeat);
                } else if (oldSeat == null) {
                    seatService.selectSeat(booking.getFlightId(), newSeat);
                }
            }

            passengers.add(PassengerCheckIn.builder()
                    .firstName(pr.firstName())
                    .lastName(pr.lastName())
                    .seatNumber(seatNumber)
                    .seatClass(findExistingSeatClass(booking, pr.firstName(), pr.lastName()))
                    .mealPreference(pr.mealPreference())
                    .seatChanged(seatChanged)
                    .build());
        }

        CheckIn checkIn = CheckIn.builder()
                .bookingNumber(request.bookingNumber())
                .customerId(booking.getCustomerId())
                .flightId(booking.getFlightId())
                .status("CHECKED_IN")
                .passengers(passengers)
                .checkedInAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        CheckIn saved = checkInRepository.save(checkIn);
        return toResponse(saved);
    }

    public CheckInResponse updateCheckIn(String bookingNumber, CheckInRequest request) {
        CheckIn checkIn = checkInRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND));

        List<PassengerCheckIn> updatedPassengers = new ArrayList<>();
        for (CheckInRequest.PassengerCheckInRequest pr : request.passengers()) {
            if (pr.seatChanged() && pr.seatNumber() != null) {
                PassengerCheckIn existing = checkIn.getPassengers().stream()
                        .filter(p -> p.getFirstName().equals(pr.firstName()) && p.getLastName().equals(pr.lastName()))
                        .findFirst().orElse(null);
                if (existing != null && !existing.getSeatNumber().equals(pr.seatNumber())) {
                    seatService.releaseSeat(checkIn.getFlightId(), existing.getSeatNumber());
                    seatService.selectSeat(checkIn.getFlightId(), pr.seatNumber());
                }
            }
            updatedPassengers.add(PassengerCheckIn.builder()
                    .firstName(pr.firstName())
                    .lastName(pr.lastName())
                    .seatNumber(pr.seatNumber() != null ? pr.seatNumber() : findExistingSeatFromCheckIn(checkIn, pr.firstName(), pr.lastName()))
                    .seatClass(findExistingSeatClassFromCheckIn(checkIn, pr.firstName(), pr.lastName()))
                    .mealPreference(pr.mealPreference())
                    .seatChanged(pr.seatChanged())
                    .build());
        }

        checkIn.setPassengers(updatedPassengers);
        checkIn.setCheckedInAt(LocalDateTime.now());
        CheckIn saved = checkInRepository.save(checkIn);
        return toResponse(saved);
    }

    private String findExistingSeat(Booking booking, String firstName, String lastName) {
        return booking.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .map(BookingPassenger::getSeatNumber)
                .findFirst().orElse(null);
    }

    private String findExistingSeatClass(Booking booking, String firstName, String lastName) {
        return booking.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .map(BookingPassenger::getSeatClass)
                .findFirst().orElse("ECONOMY");
    }

    private String findExistingSeatFromCheckIn(CheckIn checkIn, String firstName, String lastName) {
        return checkIn.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .map(PassengerCheckIn::getSeatNumber)
                .findFirst().orElse(null);
    }

    private String findExistingSeatClassFromCheckIn(CheckIn checkIn, String firstName, String lastName) {
        return checkIn.getPassengers().stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .map(PassengerCheckIn::getSeatClass)
                .findFirst().orElse("ECONOMY");
    }

    private CheckInResponse toResponse(CheckIn c) {
        return new CheckInResponse(
                c.getId(), c.getBookingNumber(), c.getCustomerId(),
                c.getFlightId(), c.getStatus(),
                c.getPassengers().stream().map(p -> new CheckInResponse.PassengerCheckInResponse(
                        p.getFirstName(), p.getLastName(), p.getSeatNumber(),
                        p.getSeatClass(), p.getMealPreference(), p.getSeatChanged()
                )).toList(),
                c.getCheckedInAt(), c.getCreatedAt()
        );
    }
}
