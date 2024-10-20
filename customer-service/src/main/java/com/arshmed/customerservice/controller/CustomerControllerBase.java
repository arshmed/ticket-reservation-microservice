package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    String createCustomer(CustomerRequest request);

    @Operation(summary = "Update an existing customer")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    void updateCustomer(String customerId, CustomerRequest request);

    @Operation(summary = "Retrieve all customers")
    List<CustomerResponse> findAll();

    @Operation(summary = "Check if a customer exists by ID")
    boolean existsById(String customerId);

    @Operation(summary = "Find a customer by ID")
    CustomerResponse findById(String customerId);
}
