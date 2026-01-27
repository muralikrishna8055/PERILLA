package com.perilla.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    // MUST match auth-service secret
    private final String secret = "same-secret-used-in-auth";

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateToken(String token) {
        extractClaims(token); // throws exception if invalid
    }

    public String extractTenant(String token) {
        return extractClaims(token).get("tenant", String.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public String extractEmployeeCode(String token) {
        return extractClaims(token).get("employeeCode", String.class);
    }
}
