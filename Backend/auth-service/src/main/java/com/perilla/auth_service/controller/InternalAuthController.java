package com.perilla.auth_service.controller;

import com.perilla.auth_service.dto.InternalUserRegisterRequest;
import com.perilla.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/auth")
@RequiredArgsConstructor

public class InternalAuthController {

    private final AuthService authService;

    // Called by Employee Service
    @PostMapping("/register-user")
    public ResponseEntity<Void>registerEmployeeUser(@RequestBody InternalUserRegisterRequest request){
        authService.registerEmployeeUser(request);
        return ResponseEntity.ok().build();
    }

    //Activate or deactivate

    @PutMapping("/users/{username}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable String username, @RequestParam boolean active){
        authService.updateUserStatus(username, active);
        return ResponseEntity.ok().build();
    }

}
