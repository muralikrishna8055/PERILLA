package com.perilla.project_service.repository;



import com.perilla.project_service.entity.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {

    long countByEmployeeCode(String employeeCode);
}

