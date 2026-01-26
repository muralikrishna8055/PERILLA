package com.perilla.employee.entity;

import com.perilla.employee.entity.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "leave_balance",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"employeeCode", "year", "leaveType", "tenantCode"}
        )
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;

    private int year;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private int totalLeaves;

    private int usedLeaves;

    @Column(nullable = false)
    private String tenantCode;

    /* =========================
       DERIVED FIELD (NO COLUMN)
       ========================= */
    @Transient
    public int getRemainingLeaves() {
        return totalLeaves - usedLeaves;
    }
}



