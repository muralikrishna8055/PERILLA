package com.perilla.employee.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManagerSyncRequest {

    @NotBlank
    private String departmentCode;

    @NotBlank
    private String managerCode;
}

