package com.perilla.employee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserCreateRequest {
    private String username;
    private String role;
    private String tenantCode;


}

