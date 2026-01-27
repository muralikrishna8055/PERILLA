package com.perilla.department_service.controller;

import com.perilla.department_service.dto.DepartmentManagerResponse;
import com.perilla.department_service.dto.DepartmentResponse;
import com.perilla.department_service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.perilla.department_service.dto.DepartmentCreateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/departments")

public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }


    @PostMapping
    public DepartmentResponse create(
            @RequestBody DepartmentCreateRequest request,
            @RequestHeader("X-Tenant-Code") String tenant,
            @RequestHeader("X-Role") String role) {

        return service.create(request, tenant, role);
    }

    @PutMapping("/{id}/manager")
    public void updateManager(
            @PathVariable Long id,
            @RequestParam String managerCode,
            @RequestHeader("X-Tenant-Code") String tenant,
            @RequestHeader("X-Role") String role) {

        service.updateManager(id, managerCode, tenant, role);
    }

    @GetMapping
    public List<DepartmentResponse> getAll(
            @RequestHeader("X-Tenant-Code") String tenant) {

        return service.getAll(tenant);
    }

    // 🔥 INTERNAL — EMPLOYEE SERVICE CALLS THIS
    @GetMapping("/internal/{departmentCode}")
    public DepartmentManagerResponse getManager(
            @PathVariable String departmentCode,
            @RequestHeader("X-Tenant-Code") String tenant) {

        return service.getManagerForDepartment(tenant, departmentCode);
    }
}