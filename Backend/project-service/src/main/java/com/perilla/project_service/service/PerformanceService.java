package com.perilla.project_service.service;

import com.perilla.project_service.entity.Task;
import com.perilla.project_service.enums.TaskStatus;
import com.perilla.project_service.repository.TaskRepository;
import com.perilla.project_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final TaskRepository taskRepository;
    private final TicketRepository ticketRepository;

    public Map<String, Object> employeePerformance(
            String employeeCode,
            String tenantId
    ) {
        List<Task> tasks =
                taskRepository.findByAssignedToAndTenantId(employeeCode, tenantId);

        long completed = tasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .count();

        long delayed = tasks.stream()
                .filter(t -> t.getEndDate() != null
                        && t.getUpdatedAt() != null
                        && t.getUpdatedAt().toLocalDate().isAfter(t.getEndDate()))
                .count();

        long tickets =
                ticketRepository.findByRaisedByAndTenantId(employeeCode, tenantId).size();

        Map<String, Object> map = new HashMap<>();
        map.put("totalTasks", tasks.size());
        map.put("completedTasks", completed);
        map.put("delayedTasks", delayed);
        map.put("ticketsRaised", tickets);

        return map;
    }
}

