package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerLoginRequest;
import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Implemented Interface Driven Controllers Pattern to abstract swagger documentation from controller
 */

public interface CustomerControllerBase {

    @Operation(summary = "Create a new customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    ResponseEntity<String> createCustomer(CustomerRequest request);

    @Operation(summary = "Update an existing customer")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    ResponseEntity<Void> updateCustomer(String customerId, CustomerRequest request);

    @Operation(summary = "Retrieve all customers")
    ResponseEntity<List<CustomerResponse>> findAll();

    @Operation(summary = "Check if a customer exists by ID")
    ResponseEntity<Boolean> existsById(String customerId);

    @Operation(summary = "Check if a customer by Email")
    ResponseEntity<Boolean> existsByEmail(String email);

    @Operation(summary = "Check if a customer by Phone Number")
    ResponseEntity<Boolean> existsByPhoneNumber(String phoneNumber);

    @Operation(summary = "Check customer credentials")
    ResponseEntity<Boolean> checkCustomerCredentials(CustomerLoginRequest request);

    @Operation(summary = "Find a customer by ID")
    ResponseEntity<CustomerResponse> findById(String customerId);

    @Operation(summary = "Find a customer by email")
    ResponseEntity<CustomerResponse> findByEmail(String customerId);

}
