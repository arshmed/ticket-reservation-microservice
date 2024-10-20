package com.arshmed.customerservice.controller;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.arshmed.customerservice.constants.RestApiList.CUSTOMER;
import static com.arshmed.customerservice.constants.RestApiList.EXISTS;

@RestController
@RequiredArgsConstructor
@RequestMapping(CUSTOMER)
public class CustomerController implements CustomerControllerBase {

    private final CustomerService customerService;

    @Override
    @PostMapping
    public String createCustomer(@RequestBody @Valid CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @Override
    @PutMapping("/{customer-id}")
    public void updateCustomer(
            @PathVariable(name = "customer-id") String customerId,
            @RequestBody @Valid CustomerRequest request) {
        customerService.updateCustomer(customerId, request);
    }

    @Override
    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @Override
    @GetMapping(EXISTS + "/{customer-id}")
    public boolean existsById(@PathVariable(name = "customer-id") String customerId) {
        return customerService.existsById(customerId);
    }

    @Override
    @GetMapping("/{customer-id}")
    public CustomerResponse findById(@PathVariable(name = "customer-id") String customerId) {
        return customerService.findById(customerId);
    }
}