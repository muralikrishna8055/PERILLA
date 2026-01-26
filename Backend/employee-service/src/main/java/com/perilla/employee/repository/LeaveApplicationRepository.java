package com.perilla.employee.repository;

import com.perilla.employee.entity.LeaveApplication;
import com.perilla.employee.entity.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveApplicationRepository
        extends JpaRepository<LeaveApplication, Long> {

    List<LeaveApplication> findByEmployeeCodeAndTenantCode(
            String employeeCode,
            String tenantCode
    );

    boolean existsByEmployeeCodeAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatus(
            String employeeCode,
            LocalDate date1,
            LocalDate date2,
            LeaveStatus status
    );

    boolean existsByEmployeeCodeAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatusIn(
            String employeeCode,
            LocalDate from,
            LocalDate to,
            List<LeaveStatus> statuses
    );


    Optional<LeaveApplication> findByIdAndTenantCode(Long id, String tenantCode);

    @Query("""
        SELECT COUNT(l)
        FROM LeaveApplication l
        WHERE l.employeeCode = :employeeCode
          AND l.tenantCode = :tenantCode
          AND l.status = 'APPROVED'
          AND (
               (MONTH(l.fromDate) = :month AND YEAR(l.fromDate) = :year)
            OR (MONTH(l.toDate) = :month AND YEAR(l.toDate) = :year)
          )
    """)
    int countApprovedLeaveDays(
            String employeeCode,
            String tenantCode,
            int month,
            int year
    );

}

