package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Implemented Interface Driven Controllers Pattern to abstract swagger documentation from controller
 */

public interface CustomerControllerBase {

    @Operation(summary = "Create a new customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request);

    @Operation(summary = "Find all customers or filter by email or phone number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    ResponseEntity<List<CustomerResponse>> findCustomers(
            @RequestParam(required = false) @Parameter(description = "Customer email") String email,
            @RequestParam(required = false) @Parameter(description = "Customer phone number") String phoneNumber
    );

    @Operation(summary = "Get the profile of the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    ResponseEntity<CustomerResponse> getCustomerProfile(
            @RequestHeader(name = "X-Auth-User-Id")
            @Parameter(description = "Authenticated user's ID", example = "12345") String authId
    );

    @Operation(summary = "Find a customer by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    ResponseEntity<CustomerResponse> findById(
            @PathVariable("customer-id") @Parameter(description = "Customer ID") String customerId
    );

    @Operation(summary = "Update an existing customer")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    ResponseEntity<Void> updateCustomer(
            @PathVariable("customer-id") @Parameter(description = "Customer ID") String customerId,
            @RequestBody @Valid CustomerRequest request
    );

    @Operation(summary = "Delete a customer by ID")
    ResponseEntity<Void> deleteCustomerById(
            @PathVariable("customer-id") @Parameter(description = "Customer ID") String customerId
    );
}