package com.arshmed.customerservice.mapper;

import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress()
        );
    }
}
