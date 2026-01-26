package com.perilla.employee.dto.response;

import com.perilla.employee.entity.enums.EmployeeStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {

    private String employeeCode;
    private String fullName;
    private String email;
    private String designation;
    private EmployeeStatus status;
    private String role;
}

