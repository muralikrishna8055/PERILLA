package com.perilla.employee.dto.response;

import com.perilla.employee.entity.enums.LeaveStatus;
import com.perilla.employee.entity.enums.LeaveType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LeaveResponse {

    private String employeeCode;
    private LeaveType leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer totalDays;
    private LeaveStatus status;
}

