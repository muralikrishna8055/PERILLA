package com.perilla.employee.controller;

import com.perilla.employee.dto.request.CreateEmployeeRequest;
import com.perilla.employee.dto.request.EmployeeDetailResponse;
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request,
            HttpServletRequest httpRequest) {

        EmployeeResponse response =
                employeeService.createEmployee(request, httpRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /* =========================
       LIST EMPLOYEES
       ========================= */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.getAllEmployees(request)
        );
    }

    @PutMapping("/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String employeeCode,
            @Valid @RequestBody UpdateEmployeeRequest request,
            HttpServletRequest httpRequest) {

        EmployeeResponse response =
                employeeService.updateEmployee(employeeCode, request, httpRequest);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{employeeCode}/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> assignDepartment(
            @PathVariable String employeeCode,
            @PathVariable Long departmentId,
            HttpServletRequest request) {

        UpdateEmployeeRequest dto = new UpdateEmployeeRequest();
        dto.setDepartmentId(departmentId);

        return ResponseEntity.ok(
                employeeService.updateEmployee(employeeCode, dto, request)
        );
    }

    @PatchMapping("/{employeeCode}/manager/{managerId}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<EmployeeResponse> assignManager(
            @PathVariable String employeeCode,
            @PathVariable Long managerId,
            HttpServletRequest request) {

        UpdateEmployeeRequest dto = new UpdateEmployeeRequest();
        dto.setManagerId(managerId);

        return ResponseEntity.ok(
                employeeService.updateEmployee(employeeCode, dto, request)
        );
    }


    @PatchMapping("/{employeeCode}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<EmployeeResponse> deactivateEmployee(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.changeEmployeeStatus(
                        employeeCode,
                        EmployeeStatus.INACTIVE,
                        request
                )
        );
    }

    @PatchMapping("/{employeeCode}/activate")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<EmployeeResponse> activateEmployee(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.changeEmployeeStatus(
                        employeeCode,
                        EmployeeStatus.ACTIVE,
                        request
                )
        );
    }

    @GetMapping("/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public ResponseEntity<EmployeeDetailResponse> getEmployeeByCode(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                employeeService.getEmployeeByCode(employeeCode, request)
        );
    }



}
