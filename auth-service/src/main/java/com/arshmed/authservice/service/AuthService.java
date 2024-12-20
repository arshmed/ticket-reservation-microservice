package com.arshmed.authservice.service;

import com.arshmed.authservice.client.CustomerServiceClient;
import com.arshmed.authservice.dto.CustomerCreateRequest;
import com.arshmed.authservice.dto.CustomerLoginRequest;
import com.arshmed.authservice.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.arshmed.authservice.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerServiceClient customerServiceClient;

    public String register(CustomerCreateRequest request) {
        checkCustomerExists(request);
        return customerServiceClient.createCustomer(request);
    }

    public String login(CustomerLoginRequest request) {
        if(!customerServiceClient.loginRequest(request)){
            throw new AuthException(INVALID_CREDENTIALS);
        }
        return "Success";
    }

    private void checkCustomerExists(CustomerCreateRequest request) {
        if(customerServiceClient.emailExists(request.email())) {
            throw new AuthException(EMAIL_EXISTS);
        }

        if(customerServiceClient.phoneNumberExists(request.phoneNumber())) {
            throw new AuthException(PHONE_NUMBER_EXISTS);
        }
    }

}
