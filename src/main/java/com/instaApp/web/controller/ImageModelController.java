package com.instaApp.web.controller;

import com.instaApp.model.entity.ImageModelEntity;
import com.instaApp.payload.response.MessageResponse;
import com.instaApp.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/images")
public class ImageModelController {
    private final ImageUploadService uploadService;

    @Autowired
    public ImageModelController(ImageUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModelEntity> getImageToPost(@PathVariable("postId") String postId) {
        ImageModelEntity imageModelEntity = uploadService.findImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(imageModelEntity, HttpStatus.OK);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModelEntity> getImageForUser(Principal principal) {
        ImageModelEntity imageModelEntity = uploadService.findImageToUser(principal);
        return new ResponseEntity<>(imageModelEntity, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        uploadService.uploadImageToUser(principal, file);
        return new ResponseEntity<>(new MessageResponse("Image upload successfully"), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId
            , @RequestParam("file") MultipartFile file
            , Principal principal) throws IOException {
        uploadService.uploadImageToPost(principal, file, Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Image upload successfully"), HttpStatus.OK);
    }
}
