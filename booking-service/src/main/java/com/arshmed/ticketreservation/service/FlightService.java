package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.request.FlightRequest;
import com.arshmed.ticketreservation.dto.response.FlightResponse;
import com.arshmed.ticketreservation.entity.Flight;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.mapper.FlightMapper;
import com.arshmed.ticketreservation.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.arshmed.ticketreservation.exception.ErrorType.*;

@RequiredArgsConstructor
@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AircraftService aircraftService;
    private final AirportService airportService;

    public Page<FlightResponse> findAll(
            int page,
            int pageSize,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String departureAirportCode,
            String destinationAirportCode) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Flight> flights;

        if (departureAirportCode != null && !departureAirportCode.isEmpty()
                && destinationAirportCode != null && !destinationAirportCode.isEmpty()) {
            flights = flightRepository.findAllByDepartureTimeBetweenAndDepartureAirportCodeAndDestinationAirportCode(
                    startDate, endDate, departureAirportCode, destinationAirportCode, pageable);
        }  else {
            flights = flightRepository.findAllByDepartureTimeBetween(startDate, endDate, pageable);
        }

        return flights.map(flightMapper::fromFlight);
    }

    public FlightResponse findById(String id) {
        return flightRepository.findById(id)
                .map(flightMapper::fromFlight)
                .orElseThrow(() -> new BookingException(FLIGHT_NOT_FOUND));
    }

    public FlightResponse createFlight(FlightRequest request) {
        validateFlightAvailability(request);

        LocalDate departureDate = request.departureTime().toLocalDate();
        LocalDate arrivalDate = request.arrivalTime().toLocalDate();

        Flight flight = flightMapper.toFlight(request);
        flight.setDepartureDate(departureDate);
        flight.setArrivalDate(arrivalDate);

        Flight savedFlight = flightRepository.save(flight);
        return flightMapper.fromFlight(savedFlight);
    }

    public List<FlightResponse> findAircraftFlights(String tailNumber) {
        aircraftService.findByTailNumber(tailNumber);
        return flightRepository.findAllByAircraftTailNumber(tailNumber)
                .stream()
                .map(flightMapper::fromFlight)
                .toList();
    }

    public List<FlightResponse> findAirportFlights(String airportCode) {
        airportService.findByAirportCode(airportCode);
        return flightRepository.findAllByDepartureAirportCode(airportCode)
                .stream()
                .map(flightMapper::fromFlight)
                .toList();
    }

    private void validateFlightAvailability(FlightRequest request) {

        aircraftService.findByTailNumber(request.aircraftTailNumber());

        if (flightRepository.findOptionalByFlightNumber(request.flightNumber()).isPresent()) {
            throw new BookingException(FLIGHT_NUMBER_CONFLICT);
        }

        airportService.findByAirportCode(request.departureAirportCode());
        airportService.findByAirportCode(request.destinationAirportCode());

        if(request.arrivalTime().isBefore(request.departureTime())){
            throw new BookingException(INVALID_TIME_RANGE);
        }

        List<Flight> aircraftFlights = flightRepository.findAllByAircraftTailNumber(request.aircraftTailNumber());

        for (Flight flight : aircraftFlights) {
            if(request.departureTime().isBefore(flight.getArrivalTime())
                    && request.arrivalTime().isAfter(flight.getDepartureTime())){
                throw new BookingException(FLIGHT_TIME_CONFLICT);
            }
        }
    }

}
