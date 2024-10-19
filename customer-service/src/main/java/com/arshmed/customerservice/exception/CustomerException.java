package com.arshmed.customerservice.exception;

import lombok.Getter;

@Getter
public class CustomerException extends RuntimeException{
    private final ErrorType errorType;

    public CustomerException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public CustomerException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }
}
