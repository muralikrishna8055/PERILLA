package com.perilla.department_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DepartmentCreateRequest {


    @NotBlank
    private String departmentName;

    @NotBlank
    private String managerCode; // REQUIRED
}

