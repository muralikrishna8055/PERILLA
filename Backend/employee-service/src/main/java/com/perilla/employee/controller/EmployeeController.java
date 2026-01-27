package com.perilla.employee.controller;

import com.perilla.employee.dto.request.CreateEmployeeRequest;
import com.perilla.employee.dto.request.EmployeeDetailResponse;
import com.perilla.employee.dto.request.ManagerSyncRequest;
import com.perilla.employee.dto.request.UpdateEmployeeRequest;
import com.perilla.employee.dto.response.EmployeeResponse;
import com.perilla.employee.entity.enums.EmployeeStatus;
import com.perilla.employee.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /* =========================
       CREATE EMPLOYEE
       ========================= */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.createEmployee(request, httpRequest));
    }

    /* =========================
       LIST EMPLOYEES
       ========================= */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            HttpServletRequest request) {

        return ResponseEntity.ok(employeeService.getAllEmployees(request));
    }

    /* =========================
       UPDATE EMPLOYEE (ADMIN)
       ========================= */
    @PutMapping("/{employeeCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String employeeCode,
            @Valid @RequestBody UpdateEmployeeRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(employeeCode, request, httpRequest)
        );
    }

    /* =========================
       ASSIGN DEPARTMENT
       ========================= */
    @PatchMapping("/{employeeCode}/department/{departmentCode}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> assignDepartment(
            @PathVariable String employeeCode,
            @PathVariable String departmentCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.assignDepartment(employeeCode, departmentCode, request)
        );
    }

    /* =========================
       INTERNAL – MANAGER SYNC
       ========================= */
    @PostMapping("/internal/manager-sync")
    public void updateManagerByDepartment(
            @RequestHeader("X-Tenant-Code") String tenant,
            @RequestBody ManagerSyncRequest request) {

        employeeService.updateManagerByDepartment(tenant, request);
    }

    /* =========================
       ACTIVATE / DEACTIVATE
       ========================= */
    @PatchMapping("/{employeeCode}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<EmployeeResponse> activate(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.changeEmployeeStatus(
                        employeeCode, EmployeeStatus.ACTIVE, request)
        );
    }

    @PatchMapping("/{employeeCode}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<EmployeeResponse> deactivate(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.changeEmployeeStatus(
                        employeeCode, EmployeeStatus.INACTIVE, request)
        );
    }

    /* =========================
       GET EMPLOYEE
       ========================= */
    @GetMapping("/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public ResponseEntity<EmployeeDetailResponse> getEmployee(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.getEmployeeByCode(employeeCode, request)
        );
    }
}

