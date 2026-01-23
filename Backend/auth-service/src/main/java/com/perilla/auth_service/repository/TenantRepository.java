package com.perilla.auth_service.repository;

import com.perilla.auth_service.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    boolean existsByCode(String code);
    Optional<Tenant> findByCode(String code);
}

