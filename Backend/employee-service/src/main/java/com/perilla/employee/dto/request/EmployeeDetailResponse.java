package com.perilla.employee.dto.request;



import com.perilla.employee.entity.enums.EmployeeStatus;
import com.perilla.employee.entity.enums.EmploymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmployeeDetailResponse {

    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String designation;

    private String departmentId;
    private String managerId;

    private LocalDate joiningDate;
    private EmploymentType employmentType;
    private Double baseSalary;

    private EmployeeStatus status;
}
