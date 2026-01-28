package com.perilla.project_service.repository;

import com.perilla.project_service.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCodeAndTenantId(
            String ticketCode,
            String tenantId
    );

    List<Ticket> findByProjectCodeAndTenantId(
            String projectCode,
            String tenantId
    );

    List<Ticket> findByRaisedByAndTenantId(
            String employeeCode,
            String tenantId
    );
}

