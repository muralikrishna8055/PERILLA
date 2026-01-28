package com.perilla.project_service.controller;

import com.perilla.project_service.entity.ProjectPost;
import com.perilla.project_service.service.ProjectPostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ProjectPostController {

    private final ProjectPostService postService;

    @PostMapping("/{projectCode}")
    public ProjectPost createPost(
            @PathVariable String projectCode,
            @RequestBody String message,
            HttpServletRequest request
    ) {
        String employeeCode = request.getAttribute("employeeCode").toString();
        String tenantId = request.getAttribute("tenantId").toString();

        return postService.createPost(
                projectCode,
                message,
                employeeCode,
                tenantId
        );
    }
}

