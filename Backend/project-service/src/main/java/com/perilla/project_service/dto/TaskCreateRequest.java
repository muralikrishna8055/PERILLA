package com.perilla.project_service.dto;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskCreateRequest {

    private String projectCode;
    private String taskTitle;
    private String description;
    private String employeeCode;
    private LocalDate dueDate;
}

