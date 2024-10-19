package com.arshmed.customerservice.dto;

import com.arshmed.customerservice.entity.Address;


public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        Address address
) {
}
