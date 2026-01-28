package com.perilla.project_service.repository;



import com.perilla.project_service.entity.Task;
import com.perilla.project_service.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTaskCodeAndTenantId(String taskCode, String tenantId);

    List<Task> findByAssignedEmployeeCodeAndTenantId(
            String employeeCode,
            String tenantId
    );

    List<Task> findByProjectCodeAndTenantId(
            String projectCode,
            String tenantId
    );

    long countByAssignedEmployeeCode(String employeeCode);

    long countByAssignedEmployeeCodeAndStatus(String employeeCode, TaskStatus status);

    List<Task> findByAssignedToAndTenantId(String employeeCode, String tenantId);
}
