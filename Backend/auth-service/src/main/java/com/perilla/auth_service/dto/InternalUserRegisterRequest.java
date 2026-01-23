package com.perilla.auth_service.dto;

import lombok.Data;

@Data
public class InternalUserRegisterRequest {

    private String username;
    private String role;
    private String tenantCode;
}
