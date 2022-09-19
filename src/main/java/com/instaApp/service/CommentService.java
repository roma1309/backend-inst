package com.instaApp.service;

import com.instaApp.dto.CommentDTO;
import com.instaApp.model.entity.CommentEntity;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    public CommentEntity createComment(Long postId, CommentDTO commentDTO, Principal principal);

    public List<CommentEntity> findAllCommentForPost(Long postId);

    public void deleteComment(Long commentId);
}
