package com.perilla.project_service.controller;

import com.perilla.project_service.service.PerformanceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @GetMapping("/employee/{employeeCode}")
    public Map<String, Object> employeePerformance(
            @PathVariable String employeeCode,
            HttpServletRequest http
    ) {
        String tenantId = http.getAttribute("tenantId").toString();
        return performanceService.employeePerformance(employeeCode, tenantId);
    }
}

