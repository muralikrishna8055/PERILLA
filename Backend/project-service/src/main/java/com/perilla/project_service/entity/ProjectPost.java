package com.perilla.project_service.entity;





import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectCode;
    private String authorCode;
    private String message;

    private LocalDateTime createdAt;

    private String tenantId;
}


