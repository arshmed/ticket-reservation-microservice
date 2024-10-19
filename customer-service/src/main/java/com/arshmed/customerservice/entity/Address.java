package com.arshmed.customerservice.entity;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
public class Address {
    private String fullAddress;
    private String city;
    private String district;
    private String zipCode;
}
