package com.instaApp.repository;

import com.instaApp.model.entity.CommentEntity;
import com.instaApp.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findByPost(PostEntity post);

    CommentEntity findByIdAndUserId(Long id, Long userId);
}
