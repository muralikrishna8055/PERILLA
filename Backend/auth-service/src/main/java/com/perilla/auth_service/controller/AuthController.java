package com.perilla.auth_service.controller;

import com.perilla.auth_service.dto.LoginRequest;
import com.perilla.auth_service.dto.LoginResponse;
import com.perilla.auth_service.dto.RegisterRequest;
import com.perilla.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>>register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok( Map.of("message", "Tenant and Admin registered successfully"));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
