package com.arshmed.customerservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(1000,"Sunucuda Bilinmeyen bir hata oluştu", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_USER(1001, "Geçersiz kullanıcı", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1002,"İstek formatı hatalı",HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_FOUND(1003, "Customer Not Found", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS(1004, "Customer Email Already Exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS(1004, "Customer Phone Number Already Exists", HttpStatus.CONFLICT);

    int code;
    String message;
    HttpStatus httpStatus;

}
