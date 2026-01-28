package com.perilla.project_service.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateRequest {

    @NotBlank
    private String projectName;

    private String description;

    @NotBlank
    private String managerCode;
}

