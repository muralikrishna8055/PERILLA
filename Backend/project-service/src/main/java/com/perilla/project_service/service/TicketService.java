package com.perilla.project_service.service;

import com.perilla.project_service.entity.Ticket;
import com.perilla.project_service.enums.TicketStatus;
import com.perilla.project_service.repository.TicketRepository;
import com.perilla.project_service.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    /* CREATE TICKET */
    public Ticket createTicket(
            String projectCode,
            String taskCode,
            String title,
            String description,
            String raisedBy,
            String tenantId
    ) {

        Ticket ticket = Ticket.builder()
                .ticketCode(CodeGenerator.generateTicketCode())
                .projectCode(projectCode)
                .taskCode(taskCode)
                .title(title)
                .description(description)
                .raisedBy(raisedBy)
                .status(TicketStatus.OPEN)
                .tenantId(tenantId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return ticketRepository.save(ticket);
    }

    /* UPDATE STATUS */
    public Ticket updateStatus(
            String ticketCode,
            TicketStatus status,
            String tenantId
    ) {

        Ticket ticket = ticketRepository
                .findByTicketCodeAndTenantId(ticketCode, tenantId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    /* VIEW PROJECT TICKETS (MANAGER / ADMIN) */
    public List<Ticket> getProjectTickets(
            String projectCode,
            String tenantId
    ) {
        return ticketRepository.findByProjectCodeAndTenantId(
                projectCode, tenantId
        );
    }

    /* VIEW MY TICKETS (EMPLOYEE) */
    public List<Ticket> getMyTickets(
            String employeeCode,
            String tenantId
    ) {
        return ticketRepository.findByRaisedByAndTenantId(
                employeeCode, tenantId
        );
    }
}

