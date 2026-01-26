package com.perilla.employee.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {

    // Required ONLY for admin override
    private LocalDate attendanceDate;

    // Used by both normal mark & override
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}


