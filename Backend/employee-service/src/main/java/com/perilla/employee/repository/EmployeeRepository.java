package com.perilla.employee.repository;

import com.perilla.employee.entity.Employee;
import com.perilla.employee.entity.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Used for CREATE validation & GET by code
    Optional<Employee> findByEmployeeCodeAndTenantCode(String employeeCode, String tenantCode);

    // Used for LIST employees (default)
    List<Employee> findAllByTenantCode(String tenantCode);

    // Used for UPDATE / DEACTIVATE safety
    Optional<Employee> findByIdAndTenantCode(Long id, String tenantCode);

    // Used for active employees only (most APIs)
    List<Employee> findAllByTenantCodeAndStatus(
            String tenantCode,
            EmployeeStatus status
    );



    // Used for email uniqueness validation
    boolean existsByEmailAndTenantCode(String email, String tenantCode);

    // Used for employeeCode uniqueness validation
    boolean existsByEmployeeCodeAndTenantCode(String employeeCode, String tenantCode);

    List<Employee> findAllByTenantCodeAndDepartmentCode(
            String tenantCode,
            String departmentCode
    );

}
