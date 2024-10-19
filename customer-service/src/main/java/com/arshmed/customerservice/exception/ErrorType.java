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
    INVALID_USER(1002, "Geçersiz kullanıcı", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1003,"İstek formatı hatalı",HttpStatus.BAD_REQUEST),
    CUSTOMER_NOT_FOUND(1005, "Customer Not Found", HttpStatus.NOT_FOUND);

    int code;
    String message;
    HttpStatus httpStatus;

}
