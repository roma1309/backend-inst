package com.instaApp.service.impl;

import com.instaApp.dto.CommentDTO;
import com.instaApp.exceptions.PostNotFoundException;
import com.instaApp.model.entity.CommentEntity;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.repository.CommentRepo;
import com.instaApp.repository.PostRepo;
import com.instaApp.repository.UserRepo;
import com.instaApp.service.CommentService;
import com.instaApp.utils.mappingDto.MappingCommentDtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final PostRepo postRepo;
    private final MappingCommentDtoUtils mappingCommentDtoUtils;

    @Autowired
    public CommentServiceImpl(UserRepo userRepo, CommentRepo commentRepo, PostRepo postRepo, MappingCommentDtoUtils mappingCommentDtoUtils) {
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.mappingCommentDtoUtils = mappingCommentDtoUtils;
    }

    @Override
    public CommentEntity createComment(Long postId, CommentDTO commentDTO, Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        PostEntity postEntity = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found username " + userEntity.getEmail()));
        CommentEntity commentEntity = mappingCommentDtoUtils.mapToCommentEntity(commentDTO, postEntity, userEntity);
        LOG.info("Saving comment for Post: {}", postEntity.getId());
        return commentRepo.save(commentEntity);
    }

    @Override
    public List<CommentEntity> findAllCommentForPost(Long postId) {
        PostEntity postEntity = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        List<CommentEntity> comments = commentRepo.findByPost(postEntity);
        return comments;

    }

    @Override
    public void deleteComment(Long commentId) {
        Optional<CommentEntity> commentEntity = commentRepo.findById(commentId);
        commentEntity.ifPresent(commentRepo::delete);
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        UserEntity userEntity = userRepo.findByEmail(username);
        if (userEntity == null) {
            new UsernameNotFoundException("Username not found with username - " + username);
        }
        return userEntity;
    }
}
