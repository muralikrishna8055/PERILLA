package com.perilla.employee.dto.request;

import com.perilla.employee.entity.enums.EmployeeStatus;
import com.perilla.employee.entity.enums.EmploymentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEmployeeRequest {



    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    private String email;

    private String phone;
    private String designation;

    @NotNull
    private Long departmentId;

    private Long managerId;

    @NotNull
    private EmploymentType employmentType;

    private Double baseSalary;

    private LocalDate joiningDate;

    /**
     * Role to be created in Auth Service
     * Example: ADMIN, HR, EMPLOYEE
     */
    private String role;
}
