package com.arshmed.customerservice.service;

import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.exception.CustomerException;
import com.arshmed.customerservice.exception.ErrorType;
import com.arshmed.customerservice.mapper.CustomerMapper;
import com.arshmed.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerException(ErrorType.CUSTOMER_NOT_FOUND));
    }
}
