package com.perilla.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_sequence")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSequence {

    @Id
    @Column(name = "tenant_code")
    private String tenantCode;

    @Column(name = "seq_value", nullable = false)
    private Long seqValue;
}

