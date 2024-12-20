package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerLoginRequest;
import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arshmed.customerservice.constants.RestApiList.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CUSTOMERS)
public class CustomerController implements CustomerControllerBase {

    private final CustomerService customerService;

    @Override
    @PostMapping
    public ResponseEntity<String> createCustomer(
            @RequestBody @Valid CustomerRequest request
    ) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @Override
    @PutMapping("/{customer-id}")
    public ResponseEntity<Void> updateCustomer(
            @PathVariable(name = "customer-id") String customerId,
            @RequestBody @Valid CustomerRequest request
    ) {
        customerService.updateCustomer(customerId, request);
        return ResponseEntity.accepted().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @Override
    @GetMapping(EXISTS + "/{customer-id}")
    public ResponseEntity<Boolean> existsById(@PathVariable(
            name = "customer-id") String customerId
    ) {
        return ResponseEntity.ok(customerService.existsById(customerId));
    }

    @Override
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> existsByEmail(
            @PathVariable(name = "email") String email
    ) {
        return ResponseEntity.ok(customerService.checkEmail(email));
    }

    @Override
    @GetMapping("/check-phone-number/{phone-number}")
    public ResponseEntity<Boolean> existsByPhoneNumber(
            @PathVariable(name = "phone-number") String phoneNumber
    ) {
        return ResponseEntity.ok(customerService.checkPhoneNumber(phoneNumber));
    }

    @Override
    @PostMapping("/check-credentials")
    public ResponseEntity<Boolean> checkCustomerCredentials(@RequestBody CustomerLoginRequest request) {
        return ResponseEntity.ok(customerService.checkCustomerCredentials(request));
    }

    @Override
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable(name = "customer-id") String customerId
    ) {
        return ResponseEntity.ok(customerService.findById(customerId));
    }

    @Override
    @GetMapping("/find-by-email/{email}")
    public ResponseEntity<CustomerResponse> findByEmail(
            @PathVariable(name = "email") String email
    ) {
        return ResponseEntity.ok(customerService.findByEmail(email));
    }
}