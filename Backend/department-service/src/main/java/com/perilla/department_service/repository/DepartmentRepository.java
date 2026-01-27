package com.perilla.department_service.repository;

import com.perilla.department_service.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByIdAndTenantCodeAndIsActiveTrue(Long id, String tenantCode);

    Optional<Department> findByTenantCodeAndDepartmentCodeAndIsActiveTrue(
            String tenantCode,
            String departmentCode
    );

    List<Department> findAllByTenantCodeAndIsActiveTrue(String tenantCode);
    Optional<Department> findByTenantCodeAndDepartmentCode(String tenantCode, String departmentCode);

    boolean existsByTenantCodeAndDepartmentCode(String tenantCode, String departmentCode);

}

