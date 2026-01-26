package com.perilla.auth_service.controller;

import com.perilla.auth_service.dto.LoginRequest;
import com.perilla.auth_service.dto.LoginResponse;
import com.perilla.auth_service.dto.RegisterRequest;
import com.perilla.auth_service.entity.RevokedToken;
import com.perilla.auth_service.repository.RevokedTokenRepository;
import com.perilla.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")


public class AuthController {

    private final AuthService authService;

    private final RevokedTokenRepository revokedTokenRepository;

    public AuthController(AuthService authService, RevokedTokenRepository revokedTokenRepository) {
        this.authService = authService;
        this.revokedTokenRepository = revokedTokenRepository;
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


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        revokedTokenRepository.save(new RevokedToken(token, new Date()));
        return ResponseEntity.ok().build();
    }

}
