package com.vti.authen;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private String secret = "mySuperSecretKeyThatIsLongEnoughForHS256Encoding123456";

    public String generateToken(String username, String role, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}