package com.arshmed.customerservice.service;

import com.arshmed.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerSecurityService {

    private final CustomerRepository customerRepository;

    public boolean isOwner(String authId, String customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customer.getAuthId().equals(authId))
                .orElse(false);
    }
}