package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.arshmed.customerservice.constants.RestApiList.CUSTOMER;

@RestController
@RequiredArgsConstructor
@RequestMapping(CUSTOMER)
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponse> getById(
            @PathVariable(name = "customer-id") String customerId
    ) {
        return ResponseEntity.ok(customerService.findById(customerId));
    }

}
