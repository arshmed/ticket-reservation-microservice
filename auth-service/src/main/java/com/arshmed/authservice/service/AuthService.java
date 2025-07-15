package com.arshmed.authservice.service;

import com.arshmed.authservice.dto.LoginRequest;
import com.arshmed.authservice.dto.RefreshTokenRequest;
import com.arshmed.authservice.dto.RegisterRequest;
import com.arshmed.authservice.dto.TokenResponse;
import com.arshmed.authservice.entity.Role;
import com.arshmed.authservice.entity.UserCredential;
import com.arshmed.authservice.event.UserCreatedEvent;
import com.arshmed.authservice.exception.AuthException;
import com.arshmed.authservice.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.arshmed.authservice.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String internalExchange;

    @Value("${rabbitmq.routing-key.customer-creation}")
    private String customerCreationRoutingKey;

    public String register(RegisterRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new AuthException(EMAIL_EXISTS);
        }

        UserCredential user = UserCredential.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        var savedUser = repository.save(user);

        var event = new UserCreatedEvent(
                savedUser.getId(),
                request.email(),
                request.firstname(),
                request.lastname()
        );
        rabbitTemplate.convertAndSend(internalExchange, customerCreationRoutingKey, event);

        return savedUser.getId();
    }

    public TokenResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException e) {
            throw new AuthException(INVALID_CREDENTIALS);
        }

        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException(INVALID_USER));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.token();
        String subject = jwtService.extractSubject(refreshToken);

        if (subject == null) {
            throw new AuthException(INVALID_TOKEN);
        }

        var userDetails = this.repository.findById(subject)
                .orElseThrow(() -> new AuthException(INVALID_USER));

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new AuthException(INVALID_TOKEN);
        }

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}