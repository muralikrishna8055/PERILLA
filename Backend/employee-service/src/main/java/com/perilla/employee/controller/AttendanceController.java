package com.perilla.employee.controller;

import com.perilla.employee.dto.request.AttendanceRequest;
import com.perilla.employee.dto.response.AttendanceResponse;
import com.perilla.employee.service.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /* =========================
       MARK ATTENDANCE
       ========================= */
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN','HR')")
    @PostMapping("/{employeeCode}")


    public ResponseEntity<AttendanceResponse> markAttendance(
            @PathVariable String employeeCode,
            @RequestBody AttendanceRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                attendanceService.markAttendance(
                        employeeCode, request, httpRequest)
        );
    }

    /* =========================
       VIEW ATTENDANCE
       ========================= */
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    @GetMapping("/{employeeCode}")


    public ResponseEntity<List<AttendanceResponse>> getAttendance(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByEmployee(
                        employeeCode, request)
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PutMapping("/{employeeCode}/override")

    public ResponseEntity<AttendanceResponse> overrideAttendance(
            @PathVariable String employeeCode,
            @RequestBody AttendanceRequest request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                attendanceService.overrideAttendance(
                        employeeCode, request, httpRequest)
        );
    }

}

