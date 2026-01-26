package com.perilla.employee.dto.request;

import com.perilla.employee.entity.enums.EmployeeStatus;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateEmployeeRequest {

    private String firstName;
    private String lastName;

    @Email
    private String email;

    private String phone;
    private String designation;
    private Long departmentId;
    private Long managerId;
    private EmployeeStatus status;
}

