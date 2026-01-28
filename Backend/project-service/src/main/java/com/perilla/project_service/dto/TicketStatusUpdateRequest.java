package com.perilla.project_service.dto;


import com.perilla.project_service.enums.TicketStatus;
import lombok.Data;

@Data
public class TicketStatusUpdateRequest {

    private TicketStatus status;
}

