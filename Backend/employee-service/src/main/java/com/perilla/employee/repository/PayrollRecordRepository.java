package com.perilla.employee.repository;

import com.perilla.employee.entity.PayrollRecord;
import com.perilla.employee.entity.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {

    Optional<PayrollRecord> findByEmployeeCodeAndMonthAndYearAndTenantCode(
            String employeeCode, int month, int year, String tenantCode
    );

    List<PayrollRecord> findByEmployeeCodeAndTenantCode(
            String employeeCode, String tenantCode
    );



}

