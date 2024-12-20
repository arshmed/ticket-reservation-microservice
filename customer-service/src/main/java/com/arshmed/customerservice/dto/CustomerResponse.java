package com.arshmed.customerservice.dto;

import com.arshmed.customerservice.entity.Address;
import com.arshmed.customerservice.entity.CustomerType;


public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        CustomerType customerType,
        String email,
        String phoneNumber,
        Address address
) {
}
