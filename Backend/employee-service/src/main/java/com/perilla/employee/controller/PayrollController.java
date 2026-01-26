package com.perilla.employee.controller;

import com.perilla.employee.dto.request.PayrollAdjustmentRequest;
import com.perilla.employee.dto.request.SalaryConfigRequest;
import com.perilla.employee.entity.PayrollRecord;
import com.perilla.employee.entity.SalaryConfig;
import com.perilla.employee.service.PayrollService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate")
    public ResponseEntity<String> generate(
            @RequestParam int month,
            @RequestParam int year,
            HttpServletRequest request) {

        payrollService.generatePayroll(month, year, request);
        return ResponseEntity.ok("Payroll generated successfully");
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/salary-config/me")
    public ResponseEntity<SalaryConfig> viewMySalaryConfig(
            HttpServletRequest request) {

        return ResponseEntity.ok(
                payrollService.viewMySalaryConfig(request)
        );
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{payrollId}/adjust")
    public ResponseEntity<PayrollRecord> adjustPayroll(
            @PathVariable Long payrollId,
            @RequestBody PayrollAdjustmentRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                payrollService.adjustPayroll(
                        payrollId, request, httpRequest)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{payrollId}/approve")
    public ResponseEntity<PayrollRecord> approvePayroll(
            @PathVariable Long payrollId,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                payrollService.approvePayroll(payrollId, request)
        );
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/me")
    public ResponseEntity<List<PayrollRecord>> myPayrolls(
            HttpServletRequest request) {

        return ResponseEntity.ok(
                payrollService.viewMyPayrolls(request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/salary-config")
    public ResponseEntity<SalaryConfig> createSalaryConfig(
            @RequestBody SalaryConfigRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                payrollService.createSalaryConfig(request, httpRequest)
        );
    }



}

