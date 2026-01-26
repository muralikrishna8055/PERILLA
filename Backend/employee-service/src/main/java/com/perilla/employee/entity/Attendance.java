package com.perilla.employee.entity;

import com.perilla.employee.entity.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "employeeCode", "attendanceDate", "tenantCode"
                })
        },
        indexes = {
                @Index(name = "idx_att_employee", columnList = "employeeCode"),
                @Index(name = "idx_att_date", columnList = "attendanceDate")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================
    // Employee Reference
    // =====================
    @Column(nullable = false)
    private String employeeCode;   // NO FK (microservice safe)

    // =====================
    // Attendance Data
    // =====================
    @Column(nullable = false)
    private LocalDate attendanceDate;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private Double workingHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    // =====================
    // Overrides
    // =====================
    private Boolean manuallyUpdated;
    private String updatedBy;

    // =====================
    // Tenant Isolation
    // =====================
    @Column(nullable = false, updatable = false)
    private String tenantCode;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
