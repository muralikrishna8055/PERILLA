package com.perilla.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "salary_config",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employeeCode", "tenantCode", "active"})
        }
)

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;
    private String tenantCode;

    private double basicPay;
    private double hra;
    private double otherAllowances;

    private double pfPercentage;
    private double taxPercentage;

    private LocalDate effectiveFrom;

    private boolean active;

    // ✅ ADD THESE
    private String createdBy;
    private LocalDateTime createdAt;
}

