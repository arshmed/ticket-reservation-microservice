package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.request.BookingRequest;
import com.arshmed.ticketreservation.dto.response.BookingResponse;
import com.arshmed.ticketreservation.entity.*;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static com.arshmed.ticketreservation.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final SeatService seatService;
    private final PricingService pricingService;
    private final DiscountService discountService;
    private final MilesService milesService;
    private final AncillaryServiceService ancillaryServiceService;
    private final SequenceGeneratorService sequenceGeneratorService;

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.18);

    public BookingResponse createBooking(BookingRequest request) {
        String roundTripGroupId = request.roundTripGroupId() != null
                ? request.roundTripGroupId() : UUID.randomUUID().toString();

        BookingResponse outboundBooking = createSingleBooking(request, roundTripGroupId, null);

        if (request.returnFlightId() != null && !request.returnFlightId().isEmpty()
                && request.returnPassengers() != null && !request.returnPassengers().isEmpty()) {
            BookingRequest returnRequest = new BookingRequest(
                    request.returnFlightId(),
                    request.customerId(),
                    request.returnPassengers(),
                    request.ancillaryServiceCodes(),
                    request.milesToRedeem(),
                    roundTripGroupId,
                    outboundBooking.id(),
                    null,
                    null
            );
            BookingResponse returnBooking = createSingleBooking(returnRequest, roundTripGroupId, outboundBooking.id());

            Booking outbound = bookingRepository.findByBookingNumber(outboundBooking.bookingNumber())
                    .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND));
            outbound.setRoundTripBookingId(returnBooking.id());
            outbound.setRoundTripGroupId(roundTripGroupId);
            bookingRepository.save(outbound);

            Booking returnB = bookingRepository.findByBookingNumber(returnBooking.bookingNumber())
                    .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND));
            returnB.setRoundTripGroupId(roundTripGroupId);
            bookingRepository.save(returnB);

            return toResponse(bookingRepository.findByBookingNumber(outboundBooking.bookingNumber())
                    .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND)));
        }

        return outboundBooking;
    }

    private BookingResponse createSingleBooking(BookingRequest request, String roundTripGroupId, String linkedBookingId) {
        Flight flight = flightService.findFlightEntity(request.flightId());
        if (LocalDateTime.now().plusMinutes(15).isAfter(flight.getDepartureTime())) {
            throw new BookingException(FLIGHT_DEPARTED);
        }
        String bookingNumber = generateBookingNumber();
        BigDecimal discountTotal = BigDecimal.ZERO;
        BigDecimal ancillaryTotal = BigDecimal.ZERO;
        BigDecimal flightChargeTotal = BigDecimal.ZERO;

        List<BookingPassenger> passengers = new ArrayList<>();

        for (BookingRequest.PassengerRequest pr : request.passengers()) {
            BigDecimal baseFare = pr.fareAmount();
            DiscountService.DiscountResult discount = discountService.calculateDiscount(
                    pr.passengerType(), pr.discountType(), pr.dateOfBirth());
            BigDecimal discountedFare = discountService.applyDiscount(baseFare, discount.discountRate());
            BigDecimal discountAmount = baseFare.subtract(discountedFare);

            String seatNumber = pr.seatNumber();
            if ("Auto-assigned".equals(seatNumber)) {
                Seat autoSeat = seatService.autoAssignSeat(request.flightId(), pr.seatClass());
                seatNumber = autoSeat.getSeatNumber();
            } else {
                seatService.selectSeat(request.flightId(), seatNumber);
            }

            BigDecimal passengerAncillaryTotal = BigDecimal.ZERO;
            List<String> ancillaryCodes = new ArrayList<>();
            if (request.ancillaryServiceCodes() != null) {
                for (String code : request.ancillaryServiceCodes()) {
                    try {
                        AncillaryService svc = ancillaryServiceService.getByCode(code);
                        passengerAncillaryTotal = passengerAncillaryTotal.add(svc.getPrice());
                        ancillaryCodes.add(code);
                    } catch (Exception ignored) {}
                }
            }

            flightChargeTotal = flightChargeTotal.add(discountedFare);
            discountTotal = discountTotal.add(discountAmount);
            ancillaryTotal = ancillaryTotal.add(passengerAncillaryTotal);

            passengers.add(BookingPassenger.builder()
                    .firstName(pr.firstName())
                    .lastName(pr.lastName())
                    .dateOfBirth(pr.dateOfBirth())
                    .passengerType(pr.passengerType())
                    .discountType(pr.discountType() != null ? pr.discountType() : "NONE")
                    .seatClass(pr.seatClass())
                    .seatNumber(seatNumber)
                    .baseFare(baseFare)
                    .discountAmount(discountAmount)
                    .finalFare(discountedFare)
                    .ancillaryServiceCodes(ancillaryCodes)
                    .build());
        }

        BigDecimal subtotal = flightChargeTotal.add(ancillaryTotal);
        BigDecimal milesDiscount = BigDecimal.ZERO;
        if (request.milesToRedeem() != null && request.milesToRedeem() > 0) {
            milesDiscount = milesService.redeemMiles(
                    request.customerId(), bookingNumber, request.milesToRedeem());
        }

        BigDecimal afterMiles = subtotal.subtract(milesDiscount);
        if (afterMiles.compareTo(BigDecimal.ZERO) < 0) afterMiles = BigDecimal.ZERO;

        BigDecimal taxAmount = afterMiles.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal grandTotal = afterMiles.add(taxAmount);

        Booking booking = Booking.builder()
                .bookingNumber(bookingNumber)
                .customerId(request.customerId())
                .flightId(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .departureAirportCode(flight.getDepartureAirportCode())
                .destinationAirportCode(flight.getDestinationAirportCode())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .passengers(passengers)
                .flightCharge(flightChargeTotal)
                .discountTotal(discountTotal)
                .ancillaryTotal(ancillaryTotal)
                .milesDiscount(milesDiscount)
                .taxAmount(taxAmount)
                .grandTotal(grandTotal)
                .status("CONFIRMED")
                .milesEarned(milesService.calculateMilesToEarn(flightChargeTotal))
                .createdAt(LocalDateTime.now())
                .roundTripBookingId(linkedBookingId)
                .roundTripGroupId(roundTripGroupId)
                .build();

        Booking saved = bookingRepository.save(booking);
        milesService.earnMiles(request.customerId(), bookingNumber, flightChargeTotal);
        return toResponse(saved);
    }

    public List<BookingResponse> getCustomerBookings(String customerId) {
        return bookingRepository.findAllByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream().map(this::toResponse).toList();
    }

    public BookingResponse getByBookingNumber(String bookingNumber) {
        return bookingRepository.findByBookingNumber(bookingNumber)
                .map(this::toResponse)
                .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND));
    }

    public List<BookingResponse> getRoundTripBookings(String groupId) {
        return bookingRepository.findAllByRoundTripGroupId(groupId)
                .stream().map(this::toResponse).toList();
    }

    public BookingResponse cancelBooking(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new BookingException(BOOKING_NOT_FOUND));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new BookingException(BAD_REQUEST_ERROR);
        }

        for (BookingPassenger p : booking.getPassengers()) {
            if (p.getSeatNumber() != null) {
                seatService.releaseSeat(booking.getFlightId(), p.getSeatNumber());
            }
        }

        if (booking.getMilesDiscount().compareTo(BigDecimal.ZERO) > 0) {
            milesService.refundMiles(booking.getCustomerId(), bookingNumber);
        }

        booking.setStatus("CANCELLED");
        Booking saved = bookingRepository.save(booking);

        if (booking.getRoundTripBookingId() != null) {
            cancelLinkedBooking(booking.getRoundTripBookingId());
        }

        return toResponse(saved);
    }

    private void cancelLinkedBooking(String bookingId) {
        bookingRepository.findById(bookingId).ifPresent(b -> {
            if (!"CANCELLED".equals(b.getStatus())) {
                for (BookingPassenger p : b.getPassengers()) {
                    if (p.getSeatNumber() != null) {
                        seatService.releaseSeat(b.getFlightId(), p.getSeatNumber());
                    }
                }
                b.setStatus("CANCELLED");
                bookingRepository.save(b);
            }
        });
    }

    private String generateBookingNumber() {
        long seq = sequenceGeneratorService.generateSequence("booking_number");
        return "TKT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%06d", seq);
    }

    private BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getId(), b.getBookingNumber(), b.getCustomerId(),
                b.getFlightId(), b.getFlightNumber(), b.getDepartureAirportCode(),
                b.getDestinationAirportCode(), b.getDepartureTime(), b.getArrivalTime(),
                b.getPassengers().stream().map(p -> new BookingResponse.PassengerResponse(
                        p.getFirstName(), p.getLastName(), p.getPassengerType(),
                        p.getDiscountType(), p.getSeatClass(), p.getSeatNumber(),
                        p.getBaseFare(), p.getDiscountAmount(), p.getFinalFare(),
                        p.getAncillaryServiceCodes())).toList(),
                b.getFlightCharge(), b.getDiscountTotal(), b.getAncillaryTotal(),
                b.getMilesDiscount(), b.getTaxAmount(), b.getGrandTotal(),
                b.getStatus(), b.getMilesEarned(), b.getCreatedAt(),
                b.getRoundTripBookingId(), b.getRoundTripGroupId());
    }
}
