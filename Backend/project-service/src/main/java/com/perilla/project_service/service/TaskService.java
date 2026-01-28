package com.perilla.project_service.service;



import com.perilla.project_service.dto.TaskCreateRequest;
import com.perilla.project_service.dto.TaskResponse;
import com.perilla.project_service.entity.Project;
import com.perilla.project_service.entity.Task;
import com.perilla.project_service.enums.TaskStatus;
import com.perilla.project_service.repository.ProjectRepository;
import com.perilla.project_service.repository.TaskRepository;
import com.perilla.project_service.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskResponse createTask(
            TaskCreateRequest request,
            String managerCode,
            String tenantId
    ) {

        // 🔒 Validate project ownership
        Project project = projectRepository
                .findByProjectCodeAndTenantId(
                        request.getProjectCode(),
                        tenantId
                )
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        if (!project.getManagerCode().equals(managerCode)) {
            throw new RuntimeException(
                    "Manager not authorized for this project"
            );
        }

        Task task = Task.builder()
                .taskCode(CodeGenerator.generateTaskCode())
                .projectCode(request.getProjectCode())
                .title(request.getTaskTitle())                 // ✅ FIX
                .description(request.getDescription())
                .assignedTo(request.getEmployeeCode())        // ✅ FIX
                .assignedBy(managerCode)                      // ✅ FIX
                .status(TaskStatus.TODO)
                .endDate(request.getDueDate())                // ✅ FIX
                .tenantId(tenantId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        taskRepository.save(task);

        return TaskResponse.builder()
                .taskCode(task.getTaskCode())
                .projectCode(task.getProjectCode())
                .taskTitle(task.getTitle())                   // ✅ FIX
                .assignedEmployeeCode(task.getAssignedTo())   // ✅ FIX
                .status(task.getStatus().name())
                .dueDate(task.getEndDate())                   // ✅ FIX
                .build();
    }






    public Task updateTaskStatus(
            String taskCode,
            TaskStatus status,
            String remark,
            String employeeCode,
            String tenantId
    ) {
        Task task = taskRepository
                .findByTaskCodeAndTenantId(taskCode, tenantId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // employee can update only own task
        if (!task.getAssignedTo().equals(employeeCode)) {
            throw new RuntimeException("Not authorized to update this task");
        }

        task.setStatus(status);
        task.setEmployeeRemark(remark);
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }



}

