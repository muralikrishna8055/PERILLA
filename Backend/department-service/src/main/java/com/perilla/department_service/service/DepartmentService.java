package com.perilla.department_service.service;

import com.perilla.department_service.client.EmployeeClient;
import com.perilla.department_service.dto.DepartmentCreateRequest;
import com.perilla.department_service.dto.DepartmentManagerResponse;
import com.perilla.department_service.dto.DepartmentResponse;
import com.perilla.department_service.dto.ManagerSyncRequest;
import com.perilla.department_service.entity.Department;

import com.perilla.department_service.exception.AccessDeniedException;
import com.perilla.department_service.exception.ResourceNotFoundException;
import com.perilla.department_service.repository.DepartmentRepository;
import com.perilla.department_service.security.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;
    private final EmployeeClient employeeClient;
    private final RoleValidator roleValidator;

    public DepartmentResponse create(
            DepartmentCreateRequest request,
            String tenant,
            String role) {

        roleValidator.requireAdmin(role);

        String deptCode = generateDepartmentCode(
                tenant,
                request.getDepartmentName()
        );

        Department dept = Department.builder()
                .tenantCode(tenant)
                .departmentCode(deptCode)
                .departmentName(request.getDepartmentName())
                .managerCode(request.getManagerCode())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(dept);

        //syncManager(dept, tenant);
        if (dept.getManagerCode() != null) {
            syncManager(dept, tenant);
        }

        return mapToResponse(dept);
    }


    public void updateManager(
            Long id,
            String managerCode,
            String tenant,
            String role) {

        roleValidator.requireAdmin(role);

        Department dept = repository
                .findByIdAndTenantCodeAndIsActiveTrue(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        dept.setManagerCode(managerCode);
        dept.setUpdatedAt(LocalDateTime.now());

        repository.save(dept);
        syncManager(dept, tenant);
    }

    public List<DepartmentResponse> getAll(String tenant) {
        return repository.findAllByTenantCodeAndIsActiveTrue(tenant)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔥 INTERNAL API FOR EMPLOYEE SERVICE
    public DepartmentManagerResponse getManagerForDepartment(
            String tenant,
            String departmentCode) {

        Department dept = repository
                .findByTenantCodeAndDepartmentCodeAndIsActiveTrue(tenant, departmentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        return new DepartmentManagerResponse(
                dept.getDepartmentCode(),
                dept.getManagerCode()
        );
    }

    private void syncManager(Department dept, String tenant) {
        employeeClient.updateManagerByDepartment(
                tenant,
                java.util.Map.of(
                        "departmentCode", dept.getDepartmentCode(),
                        "managerCode", dept.getManagerCode()
                )
        );
    }

    private DepartmentResponse mapToResponse(Department dept) {
        return DepartmentResponse.builder()
                .id(dept.getId())
                .departmentCode(dept.getDepartmentCode())
                .departmentName(dept.getDepartmentName())
                .managerCode(dept.getManagerCode())
                .build();
    }


    private String generateDepartmentCode(String tenant, String departmentName) {

        String baseCode = departmentName
                .replaceAll("[^a-zA-Z]", "")
                .toUpperCase()
                .substring(0, Math.min(3, departmentName.length()));

        String finalCode = baseCode;
        int counter = 2;

        while (repository.existsByTenantCodeAndDepartmentCode(tenant, finalCode)) {
            finalCode = baseCode + "-" + counter;
            counter++;
        }

        return finalCode;
    }

}

