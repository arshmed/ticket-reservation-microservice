package com.arshmed.customerservice.entity;

import lombok.Getter;

@Getter
public enum CustomerType {
    I ("Individual"),
    C ("Corporate");

    private final String description;

    CustomerType(String description) {
        this.description = description;
    }
}
