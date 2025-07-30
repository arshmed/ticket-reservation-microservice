package com.arshmed.customerservice.event;

public record UserCreatedEvent(
    String authId,
    String email,
    String firstname,
    String lastname
) {}