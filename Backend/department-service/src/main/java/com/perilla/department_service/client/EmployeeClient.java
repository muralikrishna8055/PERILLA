package com.perilla.department_service.client;

import com.perilla.department_service.dto.ManagerSyncRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @PutMapping("/api/employees/internal/update-manager-by-department")
    void updateManagerByDepartment(
            @RequestHeader("X-Tenant-Code") String tenantCode,
            @RequestBody Map<String, String> request
    );
}

