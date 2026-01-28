package com.perilla.project_service.entity;



import com.perilla.project_service.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;



import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "tasks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"task_code", "tenant_id"})
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskCode;
    private String projectCode;
    private String assignedTo; // employeeCode
    private String assignedBy; // managerCode

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(length = 1000)
    private String employeeRemark;

    private String tenantId;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


