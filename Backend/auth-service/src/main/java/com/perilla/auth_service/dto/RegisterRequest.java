package com.perilla.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String organizationName;
    private String tenantCode;
    private String adminUsername;
    private String password;
}


