package com.perilla.project_service.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "EMPLOYEE-SERVICE")
public interface EmployeeClient {

    @GetMapping("/internal/employees/validate")
    Boolean validateEmployee(
            @RequestParam String employeeCode,
            @RequestParam String role,
            @RequestParam String tenantId
    );
}

