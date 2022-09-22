package com.instaApp.service.impl;

import com.instaApp.dto.PostDTO;
import com.instaApp.exceptions.PostNotFoundException;
import com.instaApp.model.entity.ImageModelEntity;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.repository.ImageModelRepo;
import com.instaApp.repository.PostRepo;
import com.instaApp.repository.UserRepo;
import com.instaApp.service.PostService;
import com.instaApp.utils.mappingDto.MappingPostDtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostServiceImpl.class);
    private final PostRepo postRepo;
    private final ImageModelRepo imageModelRepo;
    private final UserRepo userRepo;
    private final MappingPostDtoUtils mappingPostDtoUtils;

    @Autowired
    public PostServiceImpl(PostRepo postRepo,
                           ImageModelRepo imageModelRepo,
                           UserRepo userRepo, MappingPostDtoUtils mappingPostDtoUtils) {
        this.postRepo = postRepo;
        this.imageModelRepo = imageModelRepo;
        this.userRepo = userRepo;
        this.mappingPostDtoUtils = mappingPostDtoUtils;
    }

    @Override
    public PostEntity createPost(Principal principal, PostDTO postDTO) {
        UserEntity userEntity = getUserByPrincipal(principal);
        PostEntity postEntity = mappingPostDtoUtils.mapToPostEntity(postDTO, userEntity);
        LOG.info("Saving Post for User {}", userEntity.getEmail());
        return postRepo.save(postEntity);
    }

    @Override
    public PostEntity findPostById(Long postId, Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        PostEntity postEntity = postRepo.findByIdAndUser(postId, userEntity);
        if (postEntity == null) {
            throw new PostNotFoundException("Post cannot be found for username " + userEntity.getEmail());
        }
        return postEntity;
    }

    @Override
    public List<PostEntity> findAllPost() {
        return postRepo.findAll();
    }

    @Override
    public List<PostEntity> findPostForUser(Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        return postRepo.findByUserOrderByCreatedDateDesc(userEntity);
    }

    @Override
    public PostEntity likePost(Long postId, String username) {
        PostEntity postEntity = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = postEntity.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username))
                .findAny();
        if (userLiked.isPresent()) {
            postEntity.setLikes(postEntity.getLikes() - 1);
            postEntity.getLikedUsers().remove(username);
        } else {
            postEntity.setLikes(postEntity.getLikes() + 1);
            postEntity.getLikedUsers().add(username);
        }
        return postRepo.save(postEntity);
    }

    @Override
    public void deleteById(Long postId, Principal principal) {
        PostEntity postEntity = findPostById(postId, principal);
        Optional<ImageModelEntity> imageModel = imageModelRepo.findById(postEntity.getId());
        postRepo.delete(postEntity);
        imageModel.ifPresent(imageModelRepo::delete);
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        UserEntity userEntity = userRepo.findByUsername(username);
        if (userEntity == null) {
            new UsernameNotFoundException("Username not found with username - " + username);
        }
        return userEntity;
    }
}
