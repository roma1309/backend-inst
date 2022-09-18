package com.instaApp.service;

import com.instaApp.dto.PostDTO;
import com.instaApp.model.entity.PostEntity;

import java.security.Principal;
import java.util.List;

public interface PostService {

    public PostEntity createPost(Principal principal, PostDTO postDTO);

    public PostEntity findPostById(Long postId, Principal principal);

    public List<PostEntity> findAllPost();

    public List<PostEntity> findPostForUser(Principal principal);

    public PostEntity likePost(Long postId, String username);

    public void deleteById(Long postId, Principal principal);

}
