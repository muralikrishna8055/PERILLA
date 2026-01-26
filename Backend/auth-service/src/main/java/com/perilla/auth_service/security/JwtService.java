package com.perilla.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key;

    private final long adminExpiry;
    private final long managerExpiry;
    private final long employeeExpiry;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiry.admin}") long adminExpiry,
            @Value("${jwt.expiry.manager}") long managerExpiry,
            @Value("${jwt.expiry.employee}") long employeeExpiry
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.adminExpiry = adminExpiry;
        this.managerExpiry = managerExpiry;
        this.employeeExpiry = employeeExpiry;
    }

    /* ================= TOKEN GENERATION (FINAL) ================= */

    public String generateToken(
            String role,
            String tenantCode,
            String email,         // for ADMIN / MANAGER
            String employeeCode   // for EMPLOYEE
    ) {

        long expiry = resolveExpiry(role);

        String subject = resolveSubject(role, email, employeeCode);

        var builder = Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .claim("tenant", tenantCode)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(key, SignatureAlgorithm.HS256);

        if ("EMPLOYEE".equals(role)) {
            builder.claim("employeeCode", employeeCode);
        }

        return builder.compact();
    }

    private String resolveSubject(String role, String email, String employeeCode) {
        if ("EMPLOYEE".equals(role)) {
            if (employeeCode == null) {
                throw new IllegalArgumentException("EmployeeCode required for EMPLOYEE token");
            }
            return employeeCode;
        }
        return email;
    }

    private long resolveExpiry(String role) {
        return switch (role) {
            case "ADMIN" -> adminExpiry;
            case "MANAGER" -> managerExpiry;
            case "EMPLOYEE" -> employeeExpiry;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    /* ================= EXTRACTION (FINAL CONTRACT) ================= */

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public String extractTenantCode(String token) {
        return extractAllClaims(token).get("tenant", String.class);
    }

    /**
     * @return employeeCode or null for ADMIN / MANAGER
     */
    public String extractEmployeeCode(String token) {
        return extractAllClaims(token).get("employeeCode", String.class);
    }

    /* ================= VALIDATION ================= */

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
