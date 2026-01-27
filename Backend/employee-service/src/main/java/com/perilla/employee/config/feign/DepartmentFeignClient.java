package com.perilla.employee.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.perilla.employee.dto.response.DepartmentManagerResponse;

@FeignClient(name = "department-service")
public interface DepartmentFeignClient {

    @GetMapping("/api/departments/internal/{departmentCode}")
    DepartmentManagerResponse getManager(
            @PathVariable String departmentCode,
            @RequestHeader("X-Tenant-Code") String tenantCode
    );
}

