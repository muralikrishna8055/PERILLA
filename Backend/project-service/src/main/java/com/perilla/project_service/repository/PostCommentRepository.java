package com.perilla.project_service.repository;



import com.perilla.project_service.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository
        extends JpaRepository<PostComment, Long> {

    List<PostComment> findByPostIdAndTenantId(
            Long postId,
            String tenantId
    );
}

