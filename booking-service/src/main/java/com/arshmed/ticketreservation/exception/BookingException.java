package com.arshmed.ticketreservation.exception;

import lombok.Getter;

@Getter
public class BookingException extends RuntimeException{
    private final ErrorType errorType;

    public BookingException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public BookingException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }
}
