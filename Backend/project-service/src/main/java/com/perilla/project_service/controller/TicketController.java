package com.perilla.project_service.controller;

import com.perilla.project_service.dto.TicketCreateRequest;
import com.perilla.project_service.dto.TicketStatusUpdateRequest;
import com.perilla.project_service.entity.Ticket;
import com.perilla.project_service.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /* CREATE TICKET (EMPLOYEE / MANAGER) */
    @PostMapping
    public Ticket create(
            @RequestBody TicketCreateRequest request,
            HttpServletRequest http
    ) {
        String employeeCode = http.getAttribute("employeeCode").toString();
        String tenantId = http.getAttribute("tenantId").toString();

        return ticketService.createTicket(
                request.getProjectCode(),
                request.getTaskCode(),
                request.getTitle(),
                request.getDescription(),
                employeeCode,
                tenantId
        );
    }

    /* UPDATE STATUS (MANAGER / ADMIN) */
    @PutMapping("/{ticketCode}/status")
    public Ticket updateStatus(
            @PathVariable String ticketCode,
            @RequestBody TicketStatusUpdateRequest request,
            HttpServletRequest http
    ) {
        String tenantId = http.getAttribute("tenantId").toString();

        return ticketService.updateStatus(
                ticketCode,
                request.getStatus(),
                tenantId
        );
    }

    /* VIEW PROJECT TICKETS */
    @GetMapping("/project/{projectCode}")
    public List<Ticket> projectTickets(
            @PathVariable String projectCode,
            HttpServletRequest http
    ) {
        String tenantId = http.getAttribute("tenantId").toString();
        return ticketService.getProjectTickets(projectCode, tenantId);
    }

    /* VIEW MY TICKETS */
    @GetMapping("/my")
    public List<Ticket> myTickets(HttpServletRequest http) {
        String employeeCode = http.getAttribute("employeeCode").toString();
        String tenantId = http.getAttribute("tenantId").toString();

        return ticketService.getMyTickets(employeeCode, tenantId);
    }
}

