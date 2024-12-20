package com.arshmed.ticketreservation.mapper;

import com.arshmed.ticketreservation.dto.request.AircraftRequest;
import com.arshmed.ticketreservation.dto.response.AircraftResponse;
import com.arshmed.ticketreservation.entity.Aircraft;
import org.springframework.stereotype.Service;

@Service
public class AircraftMapper {

    public AircraftResponse fromAircraft(Aircraft aircraft) {
        return new AircraftResponse(
                aircraft.getId(),
                aircraft.getTailNumber(),
                aircraft.getManufacturer(),
                aircraft.getModel(),
                aircraft.getNumberOfSeats()
        );
    }

    public Aircraft toAircraft(AircraftRequest request) {
        return Aircraft.builder()
                .id(request.id())
                .tailNumber(request.tailNumber())
                .manufacturer(request.manufacturer())
                .model(request.model())
                .numberOfSeats(request.numberOfSeats())
                .build();
    }
}
