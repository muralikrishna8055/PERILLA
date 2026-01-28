package com.perilla.project_service.repository;



import com.perilla.project_service.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectCode(String projectCode);

    Optional<Project> findByProjectCodeAndTenantId(String projectCode, String tenantId);
}

