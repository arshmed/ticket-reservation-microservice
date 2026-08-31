package com.arshmed.customerservice.mapper;

import com.arshmed.customerservice.dto.CustomerRequest;
import com.arshmed.customerservice.dto.CustomerResponse;
import com.arshmed.customerservice.entity.Customer;
import com.arshmed.customerservice.entity.CustomerType;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public CustomerResponse fromCustomer(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getCustomerType(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getProfilePhoto(),
                customer.getDateOfBirth(),
                customer.getGender(),
                customer.getNationality(),
                customer.getTotalMiles(),
                customer.getAvailableMiles()
        );
    }

    public Customer toCustomer(CustomerRequest request) {
        return Customer.builder()
                .id(request.id())
                .authId(request.authId())
                .firstname(request.firstname())
                .customerType(CustomerType.valueOf(request.customerType()))
                .lastname(request.lastname())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .profilePhoto(request.profilePhoto())
                .dateOfBirth(request.dateOfBirth())
                .gender(request.gender())
                .nationality(request.nationality())
                .totalMiles(0L)
                .availableMiles(0L)
                .build();
    }
}
