package com.perilla.department_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagerSyncRequest {
    private String departmentCode;
    private String managerCode;
}

