package com.perilla.project_service.dto;



import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {

    private String projectCode;
    private String projectName;
    private String description;
    private String managerCode;
    private String status;
}

