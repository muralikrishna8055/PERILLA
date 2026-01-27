package com.perilla.department_service.security;

import com.perilla.department_service.exception.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    public void requireAdmin(String role) {
        if (!"ADMIN".equals(role)) {
            throw new AccessDeniedException("ADMIN access required");
        }
    }

    public void requireAdminOrManager(String role) {
        if (!role.equals("ADMIN") && !role.equals("MANAGER")) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
