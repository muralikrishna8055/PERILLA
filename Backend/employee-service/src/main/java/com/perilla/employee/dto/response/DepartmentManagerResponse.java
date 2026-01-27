package com.perilla.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentManagerResponse {
    private String departmentCode;
    private String managerCode;
}

