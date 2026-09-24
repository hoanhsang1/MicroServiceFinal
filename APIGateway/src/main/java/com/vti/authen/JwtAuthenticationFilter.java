package com.vti.authen;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private static final String SECRET_KEY =
            "mySuperSecretKeyThatIsLongEnoughForHS256Encoding123456";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        // Không kiểm tra JWT khi login/register
        if (path.equals("/api/v1/auth/login")
                || path.equals("/api/v1/auth/register")) {

            return chain.filter(exchange);
        }

        // Lấy Authorization Header
        String authHeader =
                request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Không có Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(
                    exchange,
                    "Missing Authorization Header",
                    HttpStatus.UNAUTHORIZED
            );
        }

        // Lấy token sau chữ "Bearer "
        String token = authHeader.substring(7);

        try {

            // Kiểm tra JWT
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // Lấy username
            String username = claims.getSubject();

            // Lấy role
            String role = claims.get("role", String.class);

            // Lấy userId dưới dạng Number để tránh lỗi Long/Integer
            Number userIdNumber = claims.get("userId", Number.class);

            Long userId = null;

            if (userIdNumber != null) {
                userId = userIdNumber.longValue();
            }

            // Kiểm tra subject
            if (username == null || username.isBlank()) {
                return onError(
                        exchange,
                        "JWT does not contain username",
                        HttpStatus.UNAUTHORIZED
                );
            }

            // Gắn thông tin user vào request
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Role", role != null ? role : "")
                    .header("X-User-Id", userId != null
                            ? String.valueOf(userId)
                            : "")
                    .build();

            // Tạo exchange mới
            ServerWebExchange mutatedExchange =
                    exchange.mutate()
                            .request(mutatedRequest)
                            .build();

            // Cho request đi tiếp
            return chain.filter(mutatedExchange);

        } catch (Exception e) {

            // In lỗi thật ra console để dễ debug
            System.err.println("===== JWT ERROR =====");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=====================");

            return onError(
                    exchange,
                    "Invalid JWT token",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    private Mono<Void> onError(
            ServerWebExchange exchange,
            String message,
            HttpStatus status) {

        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(status);

        return response.setComplete();
    }
}