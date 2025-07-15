package com.arshmed.customerservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Type of the customer", example = "I or C")
@Getter
public enum CustomerType {
    I ("Individual"),
    C ("Corporate");

    private final String description;

    CustomerType(String description) {
        this.description = description;
    }
}
