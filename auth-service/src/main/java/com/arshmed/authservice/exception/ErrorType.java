package com.arshmed.authservice.exception;

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
    EMAIL_EXISTS(1005, "Email Already Exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_EXISTS(1005, "Phone Number Already Exists", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(1005, "Email or password is incorrect", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1006, "Geçersiz Token", HttpStatus.UNAUTHORIZED);

    int code;
    String message;
    HttpStatus httpStatus;

}
