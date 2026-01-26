package com.perilla.employee.entity;

import com.perilla.employee.entity.enums.EmployeeStatus;
import com.perilla.employee.entity.enums.EmploymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employeeCode", "tenantCode"}),
                @UniqueConstraint(columnNames = {"email", "tenantCode"})
        },
        indexes = {
                @Index(name = "idx_employee_tenant", columnList = "tenantCode"),
                @Index(name = "idx_employee_department", columnList = "departmentId")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================
    // Core Identity
    // =====================

    @Column(nullable = false, updatable = false)
    private String employeeCode;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phone;

    private String designation;

    // =====================
    // Organizational Mapping
    // =====================

    @Column(nullable = false)
    private Long departmentId;   // External reference (Department Service)

    private Long managerId;      // Self-reference (Employee ID)

    // =====================
    // Employment Details
    // =====================

    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    private Double baseSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    // =====================
    // Tenant Isolation
    // =====================

    @Column(nullable = false, updatable = false)
    private String tenantCode;

    // =====================
    // Audit Fields
    // =====================

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
