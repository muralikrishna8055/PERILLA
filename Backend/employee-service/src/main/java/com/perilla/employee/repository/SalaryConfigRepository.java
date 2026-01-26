package com.perilla.employee.repository;

import com.perilla.employee.entity.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaryConfigRepository extends JpaRepository<SalaryConfig, Long> {

    Optional<SalaryConfig> findByEmployeeCodeAndTenantCodeAndActiveTrue(
            String employeeCode, String tenantCode
    );

    Optional<SalaryConfig> findByEmployeeCodeAndTenantCode(
            String employeeCode, String tenantCode
    );

    List<SalaryConfig> findByTenantCodeAndActiveTrue(String tenantCode);




}

