package com.perilla.project_service.dto;



import lombok.Data;

@Data
public class TicketCreateRequest {

    private String projectCode;
    private String taskCode;

    private String title;
    private String description;
}

