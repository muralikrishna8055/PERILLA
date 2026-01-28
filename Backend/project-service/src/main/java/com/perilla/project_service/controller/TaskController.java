package com.perilla.project_service.controller;




import com.perilla.project_service.dto.TaskCreateRequest;
import com.perilla.project_service.dto.TaskResponse;
import com.perilla.project_service.dto.TaskStatusUpdateRequest;
import com.perilla.project_service.entity.Task;
import com.perilla.project_service.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(
            @RequestBody TaskCreateRequest request,
            @RequestHeader("X-Employee-Code") String managerCode,
            @RequestHeader("X-Tenant-Code") String tenantId,
            @RequestHeader("X-Role") String role
    ) {

        if (!"MANAGER".equals(role)) {
            throw new RuntimeException("Only MANAGER can create tasks");
        }

        return taskService.createTask(
                request,
                managerCode,
                tenantId
        );
    }


    @PutMapping("/{taskCode}/status")
    public Task updateStatus(
            @PathVariable String taskCode,
            @RequestBody TaskStatusUpdateRequest request,
            HttpServletRequest http
    ) {
        String employeeCode = http.getAttribute("employeeCode").toString();
        String tenantId = http.getAttribute("tenantId").toString();

        return taskService.updateTaskStatus(
                taskCode,
                request.getStatus(),
                request.getEmployeeRemark(),
                employeeCode,
                tenantId
        );
    }

}

