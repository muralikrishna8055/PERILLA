
package com.perilla.employee.config;

import com.perilla.employee.service.PayrollService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PayrollScheduler {

    private final PayrollService payrollService;

    public PayrollScheduler(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @Scheduled(cron = "0 0 2 1 * ?") // 2 AM, 1st of every month
    public void autoGeneratePayroll() {

        LocalDate now = LocalDate.now().minusMonths(1);

        // NOTE:
        // In production this will be triggered
        // via system-level admin token or internal call
        // For now keep manual + scheduler separated
    }
}

