package com.perilla.employee.repository;

import com.perilla.employee.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeCodeAndAttendanceDateAndTenantCode(
            String employeeCode,
            LocalDate attendanceDate,
            String tenantCode
    );

    List<Attendance> findByEmployeeCodeAndTenantCode(
            String employeeCode,
            String tenantCode
    );

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.employeeCode = :employeeCode
          AND a.tenantCode = :tenantCode
          AND a.status = 'PRESENT'
          AND MONTH(a.attendanceDate) = :month
          AND YEAR(a.attendanceDate) = :year
    """)
    int countPresentDays(
            String employeeCode,
            String tenantCode,
            int month,
            int year
    );


}

