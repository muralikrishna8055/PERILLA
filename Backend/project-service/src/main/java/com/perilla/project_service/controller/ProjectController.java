package com.perilla.project_service.controller;



import com.perilla.project_service.dto.ProjectCreateRequest;
import com.perilla.project_service.dto.ProjectResponse;
import com.perilla.project_service.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectResponse createProject(
            @RequestBody @Valid ProjectCreateRequest request,
            @RequestHeader("X-Tenant-Code") String tenantId,
            @RequestHeader("X-Role") String role
    ) {

        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Only ADMIN can create projects");
        }

        return projectService.createProject(request, tenantId);
    }
}

