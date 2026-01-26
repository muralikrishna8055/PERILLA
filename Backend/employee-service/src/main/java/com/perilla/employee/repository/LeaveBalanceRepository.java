package com.perilla.employee.repository;

import com.perilla.employee.entity.LeaveBalance;
import com.perilla.employee.entity.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployeeCodeAndLeaveTypeAndYearAndTenantCode(
            String employeeCode,
            LeaveType leaveType,
            Integer year,
            String tenantCode
    );
}

