package com.perilla.project_service.entity;



import com.perilla.project_service.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketCode;

    private String projectCode;
    private String taskCode;

    private String raisedBy;   // employeeCode
    private String assignedTo; // managerCode (optional)

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private String tenantId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


