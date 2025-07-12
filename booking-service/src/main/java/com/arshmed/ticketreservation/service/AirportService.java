package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.request.AirportRequest;
import com.arshmed.ticketreservation.dto.response.AirportResponse;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.mapper.AirportMapper;
import com.arshmed.ticketreservation.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.arshmed.ticketreservation.exception.ErrorType.AIRPORT_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    public List<AirportResponse> findAll() {
        return airportRepository.findAll()
                .stream()
                .map(airportMapper::fromAirport)
                .toList();
    }

    public AirportResponse findById(String id) {
        return airportRepository.findById(id)
                .map(airportMapper::fromAirport)
                .orElseThrow(() -> new BookingException(AIRPORT_NOT_FOUND));
    }

    @Cacheable(cacheNames = "airports", key = "#airportCode")
    public AirportResponse findByAirportCode(String airportCode) {
        return airportRepository.findByAirportCode(airportCode)
                .map(airportMapper::fromAirport)
                .orElseThrow(() -> new BookingException(AIRPORT_NOT_FOUND));
    }

    public String createAirport(AirportRequest request) {
        var airport = airportMapper.toAirport(request);
        return airportRepository.save(airport).getId();
    }

}
