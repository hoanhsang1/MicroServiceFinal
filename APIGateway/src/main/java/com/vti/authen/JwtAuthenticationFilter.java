package com.vti.authen;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private static final String SECRET_KEY = "mySuperSecretKeyThatIsLongEnoughForHS256Encoding123456";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();

        if (req.getURI().getPath().contains("/api/v1/auth/login")
                || req.getURI().getPath().contains("/api/v1/auth/register")) {
            return chain.filter(exchange);
        }

        String authHeader = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                String role = claims.get("role", String.class);
                Long userId = claims.get("userId", Long.class);

                ServerHttpRequest mutatedRequest = req.mutate()
                        .header("X-User-Name", username)
                        .header("X-User-Role", role)
                        .header("X-User-Id", String.valueOf(userId))
                        .build();

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                return chain.filter(mutatedExchange);
            } catch (Exception e) {
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
        }

        return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
}