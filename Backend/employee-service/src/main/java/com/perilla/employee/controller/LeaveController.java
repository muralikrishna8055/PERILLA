package com.perilla.employee.controller;

import com.perilla.employee.dto.request.ApplyLeaveRequest;
import com.perilla.employee.dto.response.LeaveResponse;
import com.perilla.employee.entity.LeaveBalance;
import com.perilla.employee.entity.enums.LeaveStatus;
import com.perilla.employee.service.LeaveService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('EMPLOYEE','MANAGER')")
    public ResponseEntity<LeaveResponse> applyLeave(
            @RequestBody ApplyLeaveRequest request,
            HttpServletRequest httpRequest) throws AccessDeniedException {
        return ResponseEntity.ok(
                leaveService.applyLeave(request, httpRequest)
        );
    }



    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<LeaveResponse>> myLeaves(HttpServletRequest request) {
        return ResponseEntity.ok(
                leaveService.getMyLeaves(request)
        );
    }

    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER')")
    public ResponseEntity<List<LeaveResponse>> employeeLeaves(
            @PathVariable String employeeCode,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                leaveService.getEmployeeLeaves(employeeCode, request)
        );
    }




    @PostMapping("/balance/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseEntity<LeaveBalance> configureLeaveBalance(
            @PathVariable String employeeCode,
            @RequestBody LeaveBalance request,
            HttpServletRequest httpRequest) {

        return ResponseEntity.ok(
                leaveService.configureLeaveBalance(employeeCode, request, httpRequest)
        );
    }

    @PostMapping("/approve/{leaveId}")
    @PreAuthorize("hasAnyRole('MANAGER','HR')")
    public ResponseEntity<LeaveResponse> approveLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveStatus status,
            HttpServletRequest request) {

        return ResponseEntity.ok(
                leaveService.updateLeaveStatus(leaveId, status, request)
        );
    }


}

