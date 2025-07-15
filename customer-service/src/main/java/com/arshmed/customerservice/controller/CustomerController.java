package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.arshmed.customerservice.constants.RestApiList.*;

@Tag(name = "Customer Controller", description = "Operations for managing customers")
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

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findCustomers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber
    ) {
        if (email != null && !email.isEmpty()) {
            CustomerResponse response = customerService.findByEmail(email);
            return ResponseEntity.ok(Collections.singletonList(response));
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            CustomerResponse response = customerService.findByPhoneNumber(phoneNumber);
            return ResponseEntity.ok(Collections.singletonList(response));
        }
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerResponse> getCustomerProfile(
            @RequestHeader("X-Auth-User-Id") String authId
    ) {
        System.out.println(authId);
        return ResponseEntity.ok(customerService.findProfileByAuthId(authId));
    }

    @Override
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> findById(
            @PathVariable(name = "customer-id") String customerId
    ) {
        return ResponseEntity.ok(customerService.findById(customerId));
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
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(
            @PathVariable(name = "customer-id") String customerId
    ) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.accepted().build();
    }

}