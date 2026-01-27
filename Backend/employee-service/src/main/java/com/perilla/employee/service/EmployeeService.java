package com.perilla.employee.service;

import com.perilla.employee.config.feign.AuthFeignClient;
import com.perilla.employee.config.feign.DepartmentFeignClient;
import com.perilla.employee.config.security.JwtService;
import com.perilla.employee.dto.request.*;
import com.perilla.employee.dto.response.DepartmentManagerResponse;
import com.perilla.employee.dto.response.EmployeeResponse;
import com.perilla.employee.dto.response.LeaveResponse;
import com.perilla.employee.entity.Employee;
import com.perilla.employee.entity.EmployeeSequence;
import com.perilla.employee.entity.enums.EmployeeStatus;
import com.perilla.employee.entity.enums.LeaveStatus;
import com.perilla.employee.exception.ResourceNotFoundException;
import com.perilla.employee.repository.EmployeeRepository;
import com.perilla.employee.repository.EmployeeSequenceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthFeignClient authFeignClient;
    private final JwtService jwtService;
    private final EmployeeSequenceRepository sequenceRepository;
    private final DepartmentFeignClient departmentFeignClient;


    public EmployeeService(EmployeeRepository employeeRepository,
                           AuthFeignClient authFeignClient,
                           JwtService jwtService, EmployeeSequenceRepository sequenceRepository, DepartmentFeignClient departmentFeignClient) {
        this.employeeRepository = employeeRepository;
        this.authFeignClient = authFeignClient;
        this.jwtService = jwtService;
        this.sequenceRepository = sequenceRepository;
        this.departmentFeignClient = departmentFeignClient;
    }

    /* =========================
       CREATE EMPLOYEE
       ========================= */
    @Transactional
    public EmployeeResponse createEmployee(
            CreateEmployeeRequest request,
            HttpServletRequest httpRequest) {

        String token = extractToken(httpRequest);
        String tenantCode = jwtService.extractTenantCode(token);

        if (employeeRepository.existsByEmailAndTenantCode(
                request.getEmail(), tenantCode)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 🔥 CALL DEPARTMENT SERVICE
        DepartmentManagerResponse dept =
                departmentFeignClient.getManager(
                        request.getDepartmentCode(),
                        tenantCode
                );

        String employeeCode = generateEmployeeCode(tenantCode);

        Employee employee = Employee.builder()
                .employeeCode(employeeCode)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .designation(request.getDesignation())
                .departmentCode(dept.getDepartmentCode())
                .managerCode(dept.getManagerCode())   // 🔥 AUTO
                .joiningDate(request.getJoiningDate())
                .employmentType(request.getEmploymentType())
                .baseSalary(request.getBaseSalary())
                .status(EmployeeStatus.ACTIVE)
                .tenantCode(tenantCode)
                .build();

        employeeRepository.save(employee);

        if (request.getRole() != null) {
            authFeignClient.createUser(
                    new AuthUserCreateRequest(employeeCode, request.getRole(), tenantCode)
            );
        }

        return mapToResponse(employee);
    }


    @Transactional
    public EmployeeResponse assignDepartment(
            String employeeCode,
            String departmentCode,
            HttpServletRequest request) {

        String tenant = jwtService.extractTenantCode(extractToken(request));

        Employee emp = employeeRepository
                .findByEmployeeCodeAndTenantCode(employeeCode, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        DepartmentManagerResponse dept =
                departmentFeignClient.getManager(departmentCode, tenant);

        emp.setDepartmentCode(dept.getDepartmentCode());
        emp.setManagerCode(dept.getManagerCode());

        employeeRepository.save(emp);
        return mapToResponse(emp);
    }








    @Transactional
    protected String generateEmployeeCode(String tenantCode) {
        EmployeeSequence sequence = sequenceRepository
                .findByTenantCode(tenantCode)
                .orElseGet(() -> {
                    EmployeeSequence seq = new EmployeeSequence();
                    seq.setTenantCode(tenantCode);
                    seq.setSeqValue(0L);
                    return seq;
                });

        long next = sequence.getSeqValue() + 1;
        sequence.setSeqValue(next);

        sequenceRepository.saveAndFlush(sequence);

        return tenantCode + "-EMP-" + String.format("%04d", next);
    }





    /* =========================
       LIST EMPLOYEES
       ========================= */
    public List<EmployeeResponse> getAllEmployees(HttpServletRequest request) {

        String token = extractToken(request);
        String tenantCode = jwtService.extractTenantCode(token);

        return employeeRepository
                .findAllByTenantCodeAndStatus(tenantCode, EmployeeStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* =========================
       COMMON UTILITIES
       ========================= */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("Missing or invalid Authorization header");
        }

        return header.substring(7);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeCode(employee.getEmployeeCode())
                .fullName(employee.getFirstName() + " " +
                        (employee.getLastName() != null ? employee.getLastName() : ""))
                .email(employee.getEmail())
                .designation(employee.getDesignation())
                .status(employee.getStatus())
                .build();
    }

    @Transactional
    public void updateManagerByDepartment(
            String tenant,
            ManagerSyncRequest req) {

        employeeRepository
                .findAllByTenantCodeAndDepartmentCode(
                        tenant, req.getDepartmentCode())
                .forEach(emp -> emp.setManagerCode(req.getManagerCode()));
    }





    @Transactional
    public EmployeeResponse updateEmployee(
            String employeeCode,
            UpdateEmployeeRequest request,
            HttpServletRequest httpRequest) {

        String tenant = jwtService.extractTenantCode(extractToken(httpRequest));

        Employee employee = employeeRepository
                .findByEmployeeCodeAndTenantCode(employeeCode, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        if (request.getFirstName() != null)
            employee.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            employee.setLastName(request.getLastName());

        if (request.getEmail() != null &&
                !request.getEmail().equals(employee.getEmail())) {

            if (employeeRepository.existsByEmailAndTenantCode(
                    request.getEmail(), tenant)) {
                throw new IllegalArgumentException("Email already exists");
            }
            employee.setEmail(request.getEmail());
        }

        if (request.getPhone() != null)
            employee.setPhone(request.getPhone());

        if (request.getDesignation() != null)
            employee.setDesignation(request.getDesignation());

        // 🔥 Department change → recalc manager
        if (request.getDepartmentCode() != null) {
            DepartmentManagerResponse dept =
                    departmentFeignClient.getManager(
                            request.getDepartmentCode(), tenant);

            employee.setDepartmentCode(dept.getDepartmentCode());
            employee.setManagerCode(dept.getManagerCode());
        }

        if (request.getStatus() != null)
            employee.setStatus(request.getStatus());

        return mapToResponse(employeeRepository.save(employee));
    }



    public EmployeeResponse changeEmployeeStatus(
            String employeeCode,
            EmployeeStatus status,
            HttpServletRequest httpRequest) {

        String token = extractToken(httpRequest);
        String tenantCode = jwtService.extractTenantCode(token);

        Employee employee = employeeRepository
                .findByEmployeeCodeAndTenantCode(employeeCode, tenantCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        employee.setStatus(status);

        Employee updated = employeeRepository.save(employee);
        return mapToResponse(updated);
    }

    public EmployeeDetailResponse getEmployeeByCode(
            String employeeCode,
            HttpServletRequest httpRequest) {

        String token = extractToken(httpRequest);
        String tenantCode = jwtService.extractTenantCode(token);

        Employee employee = employeeRepository
                .findByEmployeeCodeAndTenantCode(employeeCode, tenantCode)
                .orElseThrow(() ->
                        new EntityNotFoundException("Employee not found"));

        return mapToDetailResponse(employee);
    }

    private EmployeeDetailResponse mapToDetailResponse(Employee e) {
        return EmployeeDetailResponse.builder()
                .employeeCode(e.getEmployeeCode())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .designation(e.getDesignation())
                .departmentId(e.getDepartmentCode())
                .managerId(e.getManagerCode())
                .joiningDate(e.getJoiningDate())
                .employmentType(e.getEmploymentType())
                .baseSalary(e.getBaseSalary())
                .status(e.getStatus())
                .build();
    }

}
