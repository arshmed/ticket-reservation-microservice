package com.arshmed.authservice.controller;

import com.arshmed.authservice.dto.CustomerLoginRequest;
import com.arshmed.authservice.dto.CustomerCreateRequest;
import com.arshmed.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CustomerCreateRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody CustomerLoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
