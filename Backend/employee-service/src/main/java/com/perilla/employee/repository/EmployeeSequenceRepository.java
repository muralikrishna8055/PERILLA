package com.perilla.employee.repository;

import com.perilla.employee.entity.EmployeeSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface EmployeeSequenceRepository
        extends JpaRepository<EmployeeSequence, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<EmployeeSequence> findByTenantCode(String tenantCode);
}

