package com.perilla.auth_service.service;

import com.perilla.auth_service.dto.InternalUserRegisterRequest;
import com.perilla.auth_service.dto.LoginRequest;
import com.perilla.auth_service.dto.LoginResponse;
import com.perilla.auth_service.dto.RegisterRequest;
import com.perilla.auth_service.entity.Role;
import com.perilla.auth_service.entity.Tenant;
import com.perilla.auth_service.entity.User;
import com.perilla.auth_service.repository.TenantRepository;
import com.perilla.auth_service.repository.UserRepository;
import com.perilla.auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (tenantRepository.existsByCode(request.getTenantCode())) {
            throw new RuntimeException("Tenant code already exists");
        }

        Tenant tenant = Tenant.builder()
                .organizationName(request.getOrganizationName())
                .code(request.getTenantCode())
                .build();

        tenantRepository.save(tenant);

        User admin = User.builder()
                .username(request.getAdminUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .tenant(tenant)
                .active(true)
                .build();

        userRepository.save(admin);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token;

        if (user.getRole() == Role.ADMIN) {
            token = jwtService.generateToken(
                    "ADMIN",
                    user.getTenant().getCode(),
                    user.getUsername(), // email
                    null
            );
        } else {
            // EMPLOYEE + MANAGER
            token = jwtService.generateToken(
                    user.getRole().name(),
                    user.getTenant().getCode(),
                    null,
                    user.getUsername() // employeeCode
            );
        }


        return new LoginResponse(
                token,
                user.getRole().name(),
                user.getTenant().getCode()
        );
    }




    public void registerEmployeeUser(InternalUserRegisterRequest request) {

        Tenant tenant = tenantRepository
                .findByCode(request.getTenantCode())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode("Temp@123")) // default password
                .role(Role.valueOf(request.getRole()))
                .active(true)
                .tenant(tenant)
                .build();

        userRepository.save(user);
    }


    public void updateUserStatus(String username, boolean active) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(active);
        userRepository.save(user);
    }


}
