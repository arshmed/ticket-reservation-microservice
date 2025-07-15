package com.arshmed.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> openEndpoints = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh-token"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream().noneMatch(uri -> request.getURI().getPath().startsWith(uri));
}
