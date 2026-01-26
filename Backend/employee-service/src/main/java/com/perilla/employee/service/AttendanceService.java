package com.perilla.employee.service;

import com.perilla.employee.config.security.JwtService;
import com.perilla.employee.dto.response.AttendanceResponse;
import com.perilla.employee.dto.request.AttendanceRequest;
import com.perilla.employee.entity.Attendance;
import com.perilla.employee.entity.enums.AttendanceStatus;
import com.perilla.employee.entity.enums.LeaveStatus;
import com.perilla.employee.repository.AttendanceRepository;
import com.perilla.employee.repository.LeaveApplicationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final AttendanceRepository attendanceRepository;
    private final JwtService jwtService;

    public AttendanceService(
            LeaveApplicationRepository leaveApplicationRepository,
            AttendanceRepository attendanceRepository,
            JwtService jwtService) {

        this.leaveApplicationRepository = leaveApplicationRepository;
        this.attendanceRepository = attendanceRepository;
        this.jwtService = jwtService;
    }

    /* =========================
       MARK ATTENDANCE
       ========================= */
    public AttendanceResponse markAttendance(
            String employeeCode,
            AttendanceRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);

        String tenantCode = jwtService.extractTenantCode(token);
        String role = jwtService.extractRole(token);
        String tokenEmployeeCode = jwtService.extractEmployeeCode(token);

        // 🔐 EMPLOYEE can mark only self attendance
        if ("EMPLOYEE".equals(role) && !employeeCode.equals(tokenEmployeeCode)) {
            throw new SecurityException("Employees can mark only their own attendance");
        }

        LocalDate today = LocalDate.now();

        // 1️⃣ Leave validation
        boolean onApprovedLeave =
                leaveApplicationRepository
                        .existsByEmployeeCodeAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatus(
                                employeeCode, today, today, LeaveStatus.APPROVED);

        if (onApprovedLeave) {
            throw new IllegalStateException(
                    "Attendance cannot be marked. Employee is on approved leave.");
        }

        // 2️⃣ Fetch or create
        Attendance attendance = attendanceRepository
                .findByEmployeeCodeAndAttendanceDateAndTenantCode(
                        employeeCode, today, tenantCode)
                .orElse(
                        Attendance.builder()
                                .employeeCode(employeeCode)
                                .attendanceDate(today)
                                .tenantCode(tenantCode)
                                .status(AttendanceStatus.ABSENT)
                                .manuallyUpdated(false)
                                .build()
                );

        // 3️⃣ Validate time inputs
        if (request.getCheckInTime() != null &&
                request.getCheckOutTime() != null &&
                request.getCheckOutTime().isBefore(request.getCheckInTime())) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in time");
        }

        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());

        // 4️⃣ Calculate working hours
        if (request.getCheckInTime() != null &&
                request.getCheckOutTime() != null) {

            double hours = Duration.between(
                    request.getCheckInTime(),
                    request.getCheckOutTime()
            ).toMinutes() / 60.0;

            attendance.setWorkingHours(hours);

            attendance.setStatus(hours >= 4
                    ? AttendanceStatus.PRESENT
                    : AttendanceStatus.HALF_DAY);
        }

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }


    /* =========================
       VIEW ATTENDANCE
       ========================= */
    public List<AttendanceResponse> getAttendanceByEmployee(
            String employeeCode,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);

        return attendanceRepository
                .findByEmployeeCodeAndTenantCode(employeeCode, tenantCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* =========================
       MAPPER
       ========================= */
    private AttendanceResponse mapToResponse(Attendance a) {
        return AttendanceResponse.builder()
                .employeeCode(a.getEmployeeCode())
                .attendanceDate(a.getAttendanceDate())
                .checkInTime(a.getCheckInTime())
                .checkOutTime(a.getCheckOutTime())
                .workingHours(a.getWorkingHours())
                .status(a.getStatus())
                .build();
    }


    public AttendanceResponse overrideAttendance(
            String employeeCode,
            AttendanceRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String updatedBy = jwtService.extractUsername(token);

        LocalDate date = request.getAttendanceDate();

        Attendance attendance = attendanceRepository
                .findByEmployeeCodeAndAttendanceDateAndTenantCode(
                        employeeCode, date, tenantCode)
                .orElseThrow(() ->
                        new IllegalArgumentException("Attendance record not found"));

        attendance.setCheckInTime(request.getCheckInTime());
        attendance.setCheckOutTime(request.getCheckOutTime());

        if (request.getCheckInTime() != null &&
                request.getCheckOutTime() != null) {

            double hours = Duration.between(
                    request.getCheckInTime(),
                    request.getCheckOutTime()
            ).toMinutes() / 60.0;

            attendance.setWorkingHours(hours);
            attendance.setStatus(hours >= 4
                    ? AttendanceStatus.PRESENT
                    : AttendanceStatus.HALF_DAY);
        }

        attendance.setManuallyUpdated(true);
        attendance.setUpdatedBy(updatedBy);

        return mapToResponse(attendanceRepository.save(attendance));
    }

}
