package com.perilla.project_service.service;


import com.perilla.project_service.entity.ProjectPost;
import com.perilla.project_service.repository.ProjectPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository postRepository;

    public ProjectPost createPost(
            String projectCode,
            String message,
            String authorCode,
            String tenantId
    ) {
        ProjectPost post = ProjectPost.builder()
                .projectCode(projectCode)
                .authorCode(authorCode)
                .message(message)
                .createdAt(LocalDateTime.now())
                .tenantId(tenantId)
                .build();

        return postRepository.save(post);
    }
}

