package com.instaApp.service;

import com.instaApp.model.entity.ImageModelEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface ImageUploadService {
    public ImageModelEntity uploadImageToUser(Principal principal, MultipartFile file) throws IOException;

    public ImageModelEntity findImageToUser(Principal principal);

    public ImageModelEntity uploadImageToPost(Principal principal, MultipartFile file, Long postId) throws IOException;

    public ImageModelEntity findImageToPost(Long postId);
}
