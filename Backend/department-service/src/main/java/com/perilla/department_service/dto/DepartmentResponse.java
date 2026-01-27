package com.perilla.department_service.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {

    private Long id;
    private String departmentCode;
    private String departmentName;
    private String managerCode;
}

