package com.arshmed.authservice.client;

import com.arshmed.authservice.dto.CustomerCreateRequest;
import com.arshmed.authservice.dto.CustomerLoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "customer-service",
        url = "http://localhost:8090/api/v1/customers"
)
public interface CustomerServiceClient {

    @GetMapping("/check-email/{email}")
    Boolean emailExists(@PathVariable(name = "email") String email);

    @GetMapping("/check-phone-number/{phone-number}")
    Boolean phoneNumberExists(@PathVariable(name = "phone-number") String phoneNumber);

    @PostMapping("/check-credentials")
    Boolean loginRequest(@RequestBody CustomerLoginRequest request);

    @PostMapping
    String createCustomer(@RequestBody CustomerCreateRequest request);
}
