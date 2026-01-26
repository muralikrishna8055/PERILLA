package com.perilla.employee.entity.enums;

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

    private LocalDate attendanceDate; // REQUIRED for override
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}

