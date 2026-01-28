package com.perilla.project_service.entity;

;

import com.perilla.project_service.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String projectCode;

    @Column(nullable = false)
    private String projectName;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String managerCode;     // employeeCode of MANAGER

    @Column(nullable = false)
    private String tenantId;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;
}
