package com.arshmed.customerservice.service;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.entity.Customer;
import com.arshmed.customerservice.entity.CustomerType;
import com.arshmed.customerservice.exception.CustomerException;
import com.arshmed.customerservice.exception.ErrorType;
import com.arshmed.customerservice.mapper.CustomerMapper;
import com.arshmed.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

import static com.arshmed.customerservice.exception.ErrorType.EMAIL_ALREADY_EXISTS;
import static com.arshmed.customerservice.exception.ErrorType.PHONE_NUMBER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerException(ErrorType.CUSTOMER_NOT_FOUND));
    }

    public CustomerResponse findProfileByAuthId(String authId) {
        return repository.findByAuthId(authId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerException(ErrorType.CUSTOMER_NOT_FOUND));
    }

    public CustomerResponse findByEmail(String email) {
        return repository.findByEmail(email)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerException(ErrorType.CUSTOMER_NOT_FOUND));
    }

    public CustomerResponse findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber)
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
        var customer = customerMapper.toCustomer(request);
        if(customerRepository.existsByEmail(request.email())){
            throw new CustomerException(EMAIL_ALREADY_EXISTS);
        }
        if(request.phoneNumber() != null && customerRepository.existsByPhoneNumber(request.phoneNumber())){
            throw new CustomerException(PHONE_NUMBER_ALREADY_EXISTS);
        }
        customer.setCustomerType(CustomerType.valueOf(request.customerType()));
        repository.save(customer);
        return customer.getId();
    }

    @PreAuthorize("@customerSecurityService.isOwner(#authId, #customerId)")
    public void updateCustomer(String customerId, String authId, CustomerRequest request) {
        var customer = repository.findById(customerId)
                .orElseThrow(() -> new CustomerException(ErrorType.CUSTOMER_NOT_FOUND));
        mergeCustomer(customer, request);
        repository.save(customer);
    }

    public void deleteCustomerById(String customerId) {
        repository.deleteById(customerId);
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
