package com.perilla.employee.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeListResponse {

    private String employeeCode;
    private String fullName;
    private String designation;
    private String departmentName;
    private String status;
}

