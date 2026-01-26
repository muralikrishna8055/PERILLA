package com.perilla.employee.entity;

import com.perilla.employee.entity.enums.PayrollStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payroll_record",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employeeCode", "month", "year", "tenantCode"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;

    private String tenantCode;

    private int month;

    private int year;

    private int totalWorkingDays;
    private int presentDays;
    private int leaveDays;
    private int absentDays;

    private double basicPay;
    private double hra;
    private double otherAllowances;

    private double managerAdjustment;

    private double grossSalary;

    private double pfAmount;
    private double taxAmount;
    private double totalDeductions;

    private double netSalary;

    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    private LocalDateTime generatedAt;
    private String generatedBy;

    private LocalDateTime approvedAt;
    private String approvedBy;
}
