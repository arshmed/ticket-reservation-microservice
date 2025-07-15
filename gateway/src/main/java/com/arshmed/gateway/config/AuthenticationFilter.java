package com.arshmed.gateway.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.warn("Missing authorization header");
                    return unauthorized(exchange, "Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return unauthorized(exchange, "Invalid Authorization header format");
                }

                String token = authHeader.substring(7);

                try {
                    if (jwtUtil.isInvalid(token)) {
                        return unauthorized(exchange, "Invalid or expired JWT token");
                    }

                    Claims claims = jwtUtil.getAllClaimsFromToken(token);

                    var mutatedRequest = exchange.getRequest().mutate()
                            .header("X-Auth-User-Id", claims.getSubject())
                            .build();

                    var mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return chain.filter(mutatedExchange);

                } catch (Exception e) {
                    log.error("Error during token validation: {}", e.getMessage());
                    return unauthorized(exchange, "Unauthorized access");
                }
            }

            return chain.filter(exchange); // Eğer route public ise geç
        });
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> errorResponse = Map.of(
                "error", "Unauthorized",
                "message", message
        );

        try {
            byte[] responseBytes = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse()
                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));
        } catch (JsonProcessingException e) {
            log.error("Error writing JSON error response", e);
            return exchange.getResponse().setComplete();
        }
    }

    public static class Config {
        // Boş config class (gerekirse özelleştirilebilir)
    }
}
