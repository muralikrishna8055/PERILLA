package com.perilla.employee.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalaryConfigRequest {

    private String employeeCode;
    private double basicPay;
    private double hra;
    private double otherAllowances;
    private double pfPercentage;
    private double taxPercentage;
    private LocalDate effectiveFrom;
}

