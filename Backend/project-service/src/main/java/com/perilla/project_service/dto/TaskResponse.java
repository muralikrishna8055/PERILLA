package com.perilla.project_service.dto;



import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class TaskResponse {

    private String taskCode;
    private String projectCode;
    private String taskTitle;
    private String assignedEmployeeCode;
    private String status;
    private LocalDate dueDate;
}


