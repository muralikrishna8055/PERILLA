package com.perilla.project_service.dto;

import com.perilla.project_service.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskStatusUpdateRequest {

    private TaskStatus status;
    private String employeeRemark;
}

