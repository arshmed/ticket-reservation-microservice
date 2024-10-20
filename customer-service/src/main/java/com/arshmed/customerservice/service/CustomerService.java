package com.arshmed.customerservice.service;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.entity.Customer;
import com.arshmed.customerservice.exception.CustomerException;
import com.arshmed.customerservice.exception.ErrorType;
import com.arshmed.customerservice.mapper.CustomerMapper;
import com.arshmed.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CustomerResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .toList();
    }

    public Boolean existsById(String customerId) {
        return repository.findById(customerId).isPresent();
    }

    public String createCustomer(CustomerRequest request) {
        var customer = repository.save(customerMapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(String customerId, CustomerRequest request) {
        var customer = repository.findById(customerId)
                .orElseThrow(() -> new CustomerException(ErrorType.CUSTOMER_NOT_FOUND));
        mergeCustomer(customer, request);
        repository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if(StringUtils.isNotBlank(request.lastname())) {
            customer.setLastname(request.lastname());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(StringUtils.isNotBlank(request.phoneNumber())) {
            customer.setPhoneNumber(request.phoneNumber());
        }
        if(StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if(request.address() != null) {
            customer.setAddress(request.address());
        }
    }
}
