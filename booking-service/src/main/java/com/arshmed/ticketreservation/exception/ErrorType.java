package com.arshmed.ticketreservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(1000,"Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USER(1001, "Invalid User", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1002,"Bad Request",HttpStatus.BAD_REQUEST),
    DATA_INTEGRATION_VIOLATION_EXCEPTION(1003,"Data Integration Violation",HttpStatus.BAD_REQUEST),
    TAIL_NUMBER_EXISTS(1004,"Tail Number Already Exists",HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_FOUND(1005, "Customer Not Found", HttpStatus.NOT_FOUND),
    AIRCRAFT_NOT_FOUND(1006, "Aircraft Not Found", HttpStatus.NOT_FOUND),
    FLIGHT_NOT_FOUND(1007, "Flight Not Found", HttpStatus.NOT_FOUND),
    FLIGHT_NUMBER_CONFLICT(1008, "Flight Number Already Exists", HttpStatus.CONFLICT),
    FLIGHT_TIME_CONFLICT(1009, "Flight Time Conflict: The Aircraft Is In Use", HttpStatus.CONFLICT),
    INVALID_DATE_RANGE(1010, "Invalid Flight Dates", HttpStatus.CONFLICT),
    INVALID_TIME_RANGE(1011, "Invalid Flight Times", HttpStatus.CONFLICT),
    AIRPORT_NOT_FOUND(1012, "Airport Not Found", HttpStatus.NOT_FOUND);


    int code;
    String message;
    HttpStatus httpStatus;

}
