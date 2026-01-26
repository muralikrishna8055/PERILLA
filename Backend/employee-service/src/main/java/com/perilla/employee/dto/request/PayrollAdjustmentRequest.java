package com.perilla.employee.dto.request;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollAdjustmentRequest {

    private double adjustmentAmount;
    private String remarks;
}

