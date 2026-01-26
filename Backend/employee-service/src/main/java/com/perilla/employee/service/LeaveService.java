package com.perilla.employee.service;

import com.perilla.employee.config.security.JwtService;
import com.perilla.employee.dto.request.ApplyLeaveRequest;
import com.perilla.employee.dto.response.LeaveResponse;
import com.perilla.employee.entity.LeaveApplication;
import com.perilla.employee.entity.LeaveBalance;
import com.perilla.employee.entity.enums.LeaveStatus;
import com.perilla.employee.exception.ResourceNotFoundException;
import com.perilla.employee.repository.LeaveApplicationRepository;
import com.perilla.employee.repository.LeaveBalanceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    private final LeaveApplicationRepository leaveRepo;
    private final LeaveBalanceRepository balanceRepo;
    private final JwtService jwtService;

    public LeaveService(LeaveApplicationRepository leaveRepo,
                        LeaveBalanceRepository balanceRepo,
                        JwtService jwtService) {
        this.leaveRepo = leaveRepo;
        this.balanceRepo = balanceRepo;
        this.jwtService = jwtService;
    }

    /* =========================
       APPLY LEAVE
       ========================= */
    public LeaveResponse applyLeave(
            ApplyLeaveRequest request,
            HttpServletRequest httpRequest) throws AccessDeniedException {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String employeeCode = jwtService.extractEmployeeCode(token);

        int year = LocalDate.now().getYear();



        String tokenEmployeeCode = jwtService.extractEmployeeCode(token);

        if (!employeeCode.equals(tokenEmployeeCode)) {
            throw new AccessDeniedException("Cannot apply leave for another employee");
        }



        LeaveBalance balance = balanceRepo
                .findByEmployeeCodeAndLeaveTypeAndYearAndTenantCode(
                        employeeCode,
                        request.getLeaveType(),
                        year,
                        tenantCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Leave balance not configured"));

        int days = (int) ChronoUnit.DAYS.between(
                request.getFromDate(),
                request.getToDate()) + 1;

        if (balance.getRemainingLeaves() < days) {
            throw new IllegalArgumentException("Insufficient leave balance");
        }

        LeaveApplication leave = LeaveApplication.builder()
                .employeeCode(employeeCode)
                .leaveType(request.getLeaveType())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .totalDays(days)
                .status(LeaveStatus.APPLIED)
                .reason(request.getReason())
                .tenantCode(tenantCode)
                .build();

        boolean overlap = leaveRepo
                .existsByEmployeeCodeAndFromDateLessThanEqualAndToDateGreaterThanEqualAndStatusIn(
                        employeeCode,
                        request.getToDate(),
                        request.getFromDate(),
                        List.of(LeaveStatus.APPLIED, LeaveStatus.APPROVED)
                );


        if (overlap) {
            throw new IllegalArgumentException("Overlapping leave exists");
        }


        LeaveApplication saved = leaveRepo.save(leave);
        return mapToResponse(saved);
    }

    /* =========================
       VIEW LEAVES
       ========================= */
    public List<LeaveResponse> getEmployeeLeaves(
            String employeeCode,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);

        return leaveRepo
                .findByEmployeeCodeAndTenantCode(employeeCode, tenantCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private LeaveResponse mapToResponse(LeaveApplication leave) {
        return LeaveResponse.builder()
                .employeeCode(leave.getEmployeeCode())
                .leaveType(leave.getLeaveType())
                .fromDate(leave.getFromDate())
                .toDate(leave.getToDate())
                .totalDays(leave.getTotalDays())
                .status(leave.getStatus())
                .build();
    }

    public LeaveBalance configureLeaveBalance(
            String employeeCode,
            LeaveBalance request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);

        LeaveBalance balance = balanceRepo
                .findByEmployeeCodeAndLeaveTypeAndYearAndTenantCode(
                        employeeCode,
                        request.getLeaveType(),
                        request.getYear(),
                        tenantCode
                )
                .orElse(
                        LeaveBalance.builder()
                                .employeeCode(employeeCode)
                                .leaveType(request.getLeaveType())
                                .year(request.getYear())
                                .tenantCode(tenantCode)
                                .totalLeaves(request.getTotalLeaves()) // ✅ REQUIRED
                                .usedLeaves(0)
                                .build()
                );

// ONLY update totalLeaves
        balance.setTotalLeaves(request.getTotalLeaves());

        return balanceRepo.save(balance);

    }

    public List<LeaveResponse> getMyLeaves(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String employeeCode = jwtService.extractEmployeeCode(token);

        return leaveRepo
                .findByEmployeeCodeAndTenantCode(employeeCode, tenantCode)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public LeaveResponse updateLeaveStatus(
            Long leaveId,
            LeaveStatus status,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);



        LeaveApplication leave;
        leave = leaveRepo
                .findByIdAndTenantCode(leaveId, tenantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));


        LeaveBalance balance = balanceRepo
                .findByEmployeeCodeAndLeaveTypeAndYearAndTenantCode(
                        leave.getEmployeeCode(),
                        leave.getLeaveType(),
                        leave.getFromDate().getYear(),
                        tenantCode)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

        // If approving for the first time
        if (leave.getStatus() != LeaveStatus.APPROVED &&
                status == LeaveStatus.APPROVED) {

            balance.setUsedLeaves(
                    balance.getUsedLeaves() + leave.getTotalDays()
            );
        }

// If reverting an already approved leave
        if (leave.getStatus() == LeaveStatus.APPROVED &&
                (status == LeaveStatus.REJECTED || status == LeaveStatus.CANCELLED)) {

            balance.setUsedLeaves(
                    balance.getUsedLeaves() - leave.getTotalDays()
            );
        }

        String approver = jwtService.extractUsername(token); // email for ADMIN / MANAGER
        leave.setApprovedBy(approver);

        balanceRepo.save(balance);


        leave.setStatus(status);
        LeaveApplication saved = leaveRepo.save(leave);

        return mapToResponse(saved);
    }

}

