package com.arshmed.authservice.event;

import java.io.Serializable;

public record UserCreatedEvent(
    String authId,
    String email,
    String firstname,
    String lastname
) implements Serializable {}