package com.perilla.employee.dto.request;

import com.perilla.employee.entity.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplyLeaveRequest {

    private LeaveType leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String reason;
}

