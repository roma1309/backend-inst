package com.instaApp.service.impl;

import com.instaApp.exceptions.ImageNotFoundException;
import com.instaApp.model.entity.ImageModelEntity;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.repository.ImageModelRepo;
import com.instaApp.repository.PostRepo;
import com.instaApp.repository.UserRepo;
import com.instaApp.service.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageUploadServiceImpl.class);
    private final UserRepo userRepo;
    private final ImageModelRepo imageModelRepo;
    private final PostRepo postRepo;

    @Autowired
    public ImageUploadServiceImpl(UserRepo userRepo, ImageModelRepo imageModelRepo, PostRepo postRepo) {
        this.userRepo = userRepo;
        this.imageModelRepo = imageModelRepo;
        this.postRepo = postRepo;
    }


    @Override
    public ImageModelEntity uploadImageToUser(Principal principal, MultipartFile file) throws IOException {
        UserEntity userEntity = getUserByPrincipal(principal);
        LOG.info("Uploading image profile to user {}", userEntity.getUsername());
        ImageModelEntity userProfileImage = imageModelRepo.findByUserId(userEntity.getId());
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageModelRepo.delete(userProfileImage);
        }
        ImageModelEntity imageModelEntity = new ImageModelEntity();
        imageModelEntity.setUserId(userEntity.getId());
        imageModelEntity.setImageBytes(compressByte(file.getBytes()));
        imageModelEntity.setName(file.getOriginalFilename());
        return imageModelRepo.save(imageModelEntity);
    }

    @Override
    public ImageModelEntity findImageToUser(Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        ImageModelEntity imageModelEntity = imageModelRepo.findByUserId(userEntity.getId());
        if (!ObjectUtils.isEmpty(imageModelEntity)) {
            imageModelEntity.setImageBytes(decompressByte(imageModelEntity.getImageBytes()));
        }
        return imageModelEntity;
    }

    @Override
    public ImageModelEntity findImageToPost(Long postId) {
        ImageModelEntity imageModelEntity = imageModelRepo.findByPostId(postId);
        if (imageModelEntity == null) {
            throw new ImageNotFoundException("Cannot find image to post");
        }
        if (!ObjectUtils.isEmpty(imageModelEntity)) {
            imageModelEntity.setImageBytes(decompressByte(imageModelEntity.getImageBytes()));
        }
        return imageModelEntity;
    }

    @Override
    public ImageModelEntity uploadImageToPost(Principal principal, MultipartFile file, Long postId) throws IOException {
        UserEntity userEntity = getUserByPrincipal(principal);
        PostEntity postEntity = userEntity.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toSinglePostCollector());
        ImageModelEntity imageModelEntity = new ImageModelEntity();
        imageModelEntity.setName(file.getOriginalFilename());
        imageModelEntity.setPostId(postEntity.getId());
        imageModelEntity.setImageBytes(compressByte(file.getBytes()));
        LOG.info("Uploading image  to post {}", postEntity.getId());
        return imageModelRepo.save(imageModelEntity);
    }

    private byte[] compressByte(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress bytes");
        }
        LOG.info("Compress Image Byte size " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private byte[] decompressByte(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress bytes");
        }
        return outputStream.toByteArray();
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        UserEntity userEntity = userRepo.findByUsername(username);
        if (userEntity == null) {
            new UsernameNotFoundException("Username not found with username - " + username);
        }
        return userEntity;
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
