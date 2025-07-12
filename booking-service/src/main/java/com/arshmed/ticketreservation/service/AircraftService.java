package com.arshmed.ticketreservation.service;

import com.arshmed.ticketreservation.dto.request.AircraftRequest;
import com.arshmed.ticketreservation.dto.response.AircraftResponse;
import com.arshmed.ticketreservation.exception.BookingException;
import com.arshmed.ticketreservation.mapper.AircraftMapper;
import com.arshmed.ticketreservation.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.arshmed.ticketreservation.exception.ErrorType.AIRCRAFT_NOT_FOUND;
import static com.arshmed.ticketreservation.exception.ErrorType.TAIL_NUMBER_EXISTS;

@RequiredArgsConstructor
@Service
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;


    public AircraftResponse findById(String id) {
        return aircraftRepository.findById(id).map(aircraftMapper::fromAircraft)
                .orElseThrow(() -> new BookingException(AIRCRAFT_NOT_FOUND));
    }

    public List<AircraftResponse> findAll() {
        return aircraftRepository.findAll()
                .stream()
                .map(aircraftMapper::fromAircraft)
                .toList();
    }

    public String createAircraft(AircraftRequest request) {
        if(aircraftRepository.findByTailNumber(request.tailNumber()).isPresent()) {
            throw new BookingException(TAIL_NUMBER_EXISTS);
        }
        var aircraft = aircraftMapper.toAircraft(request);
        aircraftRepository.save(aircraft);
        return aircraft.getId();
    }

    @Cacheable(cacheNames = "aircrafts", key = "#tailNumber")
    public AircraftResponse findByTailNumber(String tailNumber) {
        return aircraftRepository.findByTailNumber(tailNumber)
                .map(aircraftMapper::fromAircraft)
                .orElseThrow(() -> new BookingException(AIRCRAFT_NOT_FOUND));
    }

}
