package com.perilla.project_service.service;



import com.perilla.project_service.dto.ProjectCreateRequest;
import com.perilla.project_service.dto.ProjectResponse;
import com.perilla.project_service.entity.Project;
import com.perilla.project_service.enums.ProjectStatus;
import com.perilla.project_service.repository.ProjectRepository;
import com.perilla.project_service.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(
            ProjectCreateRequest request,
            String tenantId
    ) {

        Project project = Project.builder()
                .projectCode(CodeGenerator.generateProjectCode())
                .projectName(request.getProjectName())
                .description(request.getDescription())
                .managerCode(request.getManagerCode())
                .tenantId(tenantId)
                .status(ProjectStatus.PLANNED)
                .build();

        projectRepository.save(project);

        return ProjectResponse.builder()
                .projectCode(project.getProjectCode())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .managerCode(project.getManagerCode())
                .status(project.getStatus().name())
                .build();
    }
}

