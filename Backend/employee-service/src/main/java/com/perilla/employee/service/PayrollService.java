package com.perilla.employee.service;

import com.perilla.employee.config.security.JwtService;
import com.perilla.employee.dto.request.PayrollAdjustmentRequest;
import com.perilla.employee.dto.request.SalaryConfigRequest;
import com.perilla.employee.entity.PayrollRecord;
import com.perilla.employee.entity.SalaryConfig;
import com.perilla.employee.entity.enums.PayrollStatus;
import com.perilla.employee.repository.AttendanceRepository;
import com.perilla.employee.repository.LeaveApplicationRepository;
import com.perilla.employee.repository.PayrollRecordRepository;
import com.perilla.employee.repository.SalaryConfigRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayrollService {

    private final PayrollRecordRepository payrollRepo;
    private final SalaryConfigRepository salaryRepo;
    private final AttendanceRepository attendanceRepo;
    private final LeaveApplicationRepository leaveRepo;
    private final JwtService jwtService;

    public PayrollService(
            PayrollRecordRepository payrollRepo,
            SalaryConfigRepository salaryRepo,
            AttendanceRepository attendanceRepo,
            LeaveApplicationRepository leaveRepo,
            JwtService jwtService) {

        this.payrollRepo = payrollRepo;
        this.salaryRepo = salaryRepo;
        this.attendanceRepo = attendanceRepo;
        this.leaveRepo = leaveRepo;
        this.jwtService = jwtService;
    }

    /* ==========================
       GENERATE PAYROLL (ADMIN)
       ========================== */
    public void generatePayroll(int month, int year, HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String admin = jwtService.extractUsername(token);

        List<SalaryConfig> configs =
                salaryRepo.findByTenantCodeAndActiveTrue(tenantCode);


        for (SalaryConfig config : configs) {

            boolean exists = payrollRepo
                    .findByEmployeeCodeAndMonthAndYearAndTenantCode(
                            config.getEmployeeCode(), month, year, tenantCode)
                    .isPresent();

            if (exists) continue;

            int presentDays =
                    attendanceRepo.countPresentDays(
                            config.getEmployeeCode(), tenantCode, month, year);

            int leaveDays =
                    leaveRepo.countApprovedLeaveDays(
                            config.getEmployeeCode(), tenantCode, month, year);

            int totalDays = LocalDate.of(year, month, 1).lengthOfMonth();
            int absentDays = totalDays - (presentDays + leaveDays);

            double gross =
                    config.getBasicPay()
                            + config.getHra()
                            + config.getOtherAllowances();

            double pf = config.getBasicPay() * config.getPfPercentage() / 100;
            double tax = gross * config.getTaxPercentage() / 100;

            double absentDeduction = (gross / totalDays) * absentDays;

            double net =
                    gross - pf - tax - absentDeduction;

            PayrollRecord payroll = PayrollRecord.builder()
                    .employeeCode(config.getEmployeeCode())
                    .tenantCode(tenantCode)
                    .month(month)
                    .year(year)
                    .totalWorkingDays(totalDays)
                    .presentDays(presentDays)
                    .leaveDays(leaveDays)
                    .absentDays(absentDays)
                    .basicPay(config.getBasicPay())
                    .hra(config.getHra())
                    .otherAllowances(config.getOtherAllowances())
                    .managerAdjustment(0)
                    .grossSalary(gross)
                    .pfAmount(pf)
                    .taxAmount(tax)
                    .totalDeductions(pf + tax + absentDeduction)
                    .netSalary(net)
                    .status(PayrollStatus.GENERATED)
                    .generatedBy(admin)
                    .generatedAt(LocalDateTime.now())
                    .build();

            payrollRepo.save(payroll);
        }
    }


    public SalaryConfig viewMySalaryConfig(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String employeeCode = jwtService.extractEmployeeCode(token);

        return salaryRepo.findByEmployeeCodeAndTenantCode(employeeCode, tenantCode)
                .orElseThrow(() ->
                        new IllegalArgumentException("Salary config not found"));
    }


    public PayrollRecord adjustPayroll(
            Long payrollId,
            PayrollAdjustmentRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String manager = jwtService.extractUsername(token);

        PayrollRecord payroll = payrollRepo.findById(payrollId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Payroll record not found"));

        if (!payroll.getTenantCode().equals(tenantCode)) {
            throw new SecurityException("Tenant mismatch");
        }

        payroll.setManagerAdjustment(request.getAdjustmentAmount());

        double newNet =
                payroll.getNetSalary() + request.getAdjustmentAmount();

        payroll.setNetSalary(newNet);
        payroll.setStatus(PayrollStatus.MODIFIED);

        return payrollRepo.save(payroll);
    }


    public PayrollRecord approvePayroll(
            Long payrollId,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String admin = jwtService.extractUsername(token);

        PayrollRecord payroll = payrollRepo.findById(payrollId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Payroll not found"));

        if (!payroll.getTenantCode().equals(tenantCode)) {
            throw new SecurityException("Tenant mismatch");
        }

        payroll.setStatus(PayrollStatus.APPROVED);
        payroll.setApprovedBy(admin);
        payroll.setApprovedAt(LocalDateTime.now());

        return payrollRepo.save(payroll);
    }


    public List<PayrollRecord> viewMyPayrolls(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String employeeCode = jwtService.extractEmployeeCode(token);

        return payrollRepo.findByEmployeeCodeAndTenantCode(
                employeeCode, tenantCode);
    }


    public SalaryConfig createSalaryConfig(
            SalaryConfigRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String tenantCode = jwtService.extractTenantCode(token);
        String admin = jwtService.extractUsername(token);

        // Deactivate old config if exists
        salaryRepo.findByEmployeeCodeAndTenantCode(
                request.getEmployeeCode(), tenantCode
        ).ifPresent(old -> {
            old.setActive(false);
            salaryRepo.save(old);
        });

        SalaryConfig config = SalaryConfig.builder()
                .employeeCode(request.getEmployeeCode())
                .tenantCode(tenantCode)
                .basicPay(request.getBasicPay())
                .hra(request.getHra())
                .otherAllowances(request.getOtherAllowances())
                .pfPercentage(request.getPfPercentage())
                .taxPercentage(request.getTaxPercentage())
                .effectiveFrom(request.getEffectiveFrom())
                .active(true)
                .createdAt(LocalDateTime.now())
                .createdBy(admin)
                .build();

        return salaryRepo.save(config);
    }



}

