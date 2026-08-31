package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.PageDto;
import com.arshmed.ticketreservation.dto.request.FlightRequest;
import com.arshmed.ticketreservation.dto.response.FlightDetailResponse;
import com.arshmed.ticketreservation.dto.response.FlightResponse;
import com.arshmed.ticketreservation.entity.Flight;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.mapper.FlightMapper;
import com.arshmed.ticketreservation.repository.FlightRepository;
import com.arshmed.ticketreservation.service.SeatService;
import com.arshmed.ticketreservation.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.arshmed.ticketreservation.exception.ErrorType.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    private final AircraftService aircraftService;
    private final AirportService airportService;
    private final SeatService seatService;
    private final PricingService pricingService;

//    @Cacheable(
//            value = "flights",
//            key = "'findAll:' + #page + ':' + #pageSize + ':' + T(java.util.Objects).toString(#startDate, 'null') + ':' + T(java.util.Objects).toString(#endDate, 'null') + ':' + T(java.util.Objects).toString(#departureAirportCode, 'null') + ':' + T(java.util.Objects).toString(#destinationAirportCode, 'null')",
//            unless = "#result == null or #result.content().isEmpty()"
//    )
    public PageDto<FlightResponse> findAll( // <-- Dönüş tipini değiştirin
                                            int page,
                                            int pageSize,
                                            LocalDateTime startDate,
                                            LocalDateTime endDate,
                                            String departureAirportCode,
                                            String destinationAirportCode) {

        log.info("===> CACHE MISS! Uçuşlar veritabanından sorgulanıyor... Parametreler: page={}, pageSize={}, departure={}, destination={}",
                page, pageSize, departureAirportCode, destinationAirportCode);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Flight> flights;

        if (startDate == null && endDate == null) {
            if (departureAirportCode != null && !departureAirportCode.isEmpty()
                    && destinationAirportCode != null && !destinationAirportCode.isEmpty()) {
                flights = flightRepository.findAllByDepartureAirportCodeAndDestinationAirportCode(
                        departureAirportCode, destinationAirportCode, pageable);
            } else if (departureAirportCode != null && !departureAirportCode.isEmpty()) {
                flights = flightRepository.findAllByDepartureAirportCode(departureAirportCode, pageable);
            } else if (destinationAirportCode != null && !destinationAirportCode.isEmpty()) {
                flights = flightRepository.findAllByDestinationAirportCode(destinationAirportCode, pageable);
            } else {
                flights = flightRepository.findAll(pageable);
            }
        } else if (departureAirportCode != null && !departureAirportCode.isEmpty()
                && destinationAirportCode != null && !destinationAirportCode.isEmpty()) {
            flights = flightRepository.findAllByDepartureTimeBetweenAndDepartureAirportCodeAndDestinationAirportCode(
                    startDate, endDate, departureAirportCode, destinationAirportCode, pageable);
        } else {
            flights = flightRepository.findAllByDepartureTimeBetween(startDate, endDate, pageable);
        }

        // Page<Flight>'i Page<FlightResponse>'a map'leyin
        Page<FlightResponse> flightResponsePage = flights.map(flightMapper::fromFlight);

        // Page<FlightResponse>'ı PageDto<FlightResponse>'a dönüştürün ve return edin
        return new PageDto<>(
                flightResponsePage.getContent(),
                flightResponsePage.getTotalPages(),
                flightResponsePage.getTotalElements(),
                flightResponsePage.getNumber(),
                flightResponsePage.getSize()
        );
    }

//    @Cacheable(cacheNames = "flight_id", key = "#id", unless = "#result == null")
    public FlightResponse findById(String id) {
        return flightRepository.findById(id)
                .map(flightMapper::fromFlight)
                .orElseThrow(() -> new BookingException(FLIGHT_NOT_FOUND));
    }

//    @CacheEvict(value = "flights", allEntries = true)
    public FlightResponse createFlight(FlightRequest request) {
        validateFlightAvailability(request);
        System.out.println("here");

        LocalDate departureDate = request.departureTime().toLocalDate();
        LocalDate arrivalDate = request.arrivalTime().toLocalDate();

        Flight flight = flightMapper.toFlight(request);
        flight.setDepartureDate(departureDate);
        flight.setArrivalDate(arrivalDate);

        System.out.println("here1");

        Flight savedFlight = flightRepository.save(flight);
        seatService.generateSeatsForFlight(savedFlight);
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

    public Flight findFlightEntity(String id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new BookingException(FLIGHT_NOT_FOUND));
    }

    public FlightDetailResponse getFlightDetail(String id) {
        Flight flight = findFlightEntity(id);

        com.arshmed.ticketreservation.dto.response.AircraftResponse aircraft =
                aircraftService.findByTailNumber(flight.getAircraftTailNumber());
        com.arshmed.ticketreservation.dto.response.AirportResponse depAirport =
                airportService.findByAirportCode(flight.getDepartureAirportCode());
        com.arshmed.ticketreservation.dto.response.AirportResponse destAirport =
                airportService.findByAirportCode(flight.getDestinationAirportCode());

        Map<String, Map<String, Long>> seatStats = seatService.getSeatStatsByFlight(id);
        Map<String, FlightDetailResponse.SeatClassInfo> seatClasses = new LinkedHashMap<>();
        for (String seatClass : List.of("ECONOMY", "BUSINESS", "FIRST")) {
            Map<String, Long> classStats = seatStats.get(seatClass);
            int totalSeats = classStats.get("total").intValue();
            int bookedSeats = classStats.get("booked").intValue();
            BigDecimal basePrice = flight.getFlightCharge();
            BigDecimal currentPrice = pricingService.calculateDynamicPrice(
                    basePrice, seatClass, totalSeats, bookedSeats);
            seatClasses.put(seatClass, new FlightDetailResponse.SeatClassInfo(
                    seatClass, totalSeats, totalSeats - bookedSeats,
                    basePrice.multiply(pricingService.getClassMultiplier(seatClass)),
                    currentPrice, "USD"));
        }

        return new FlightDetailResponse(
                flight.getId(), flight.getFlightNumber(),
                flight.getAircraftTailNumber(),
                aircraft.manufacturer(), aircraft.model(),
                flight.getDepartureAirportCode(), depAirport.airportName(),
                flight.getDestinationAirportCode(), destAirport.airportName(),
                depAirport.city(), destAirport.city(),
                flight.getDepartureDate(), flight.getArrivalDate(),
                flight.getDepartureTime(), flight.getArrivalTime(),
                flight.getFlightCharge(), seatClasses);
    }
}
