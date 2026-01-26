package com.perilla.employee.entity;

import com.perilla.employee.entity.enums.LeaveStatus;
import com.perilla.employee.entity.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "leave_application",
        indexes = {
                @Index(columnList = "employeeCode, tenantCode"),
                @Index(columnList = "tenantCode")
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private LocalDate fromDate;
    private LocalDate toDate;

    private Integer totalDays;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    private String reason;

    private String approvedBy;

    private String tenantCode;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

