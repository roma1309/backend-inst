package com.instaApp.web.controller;

import com.instaApp.dto.PostDTO;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.payload.response.MessageResponse;
import com.instaApp.service.PostService;
import com.instaApp.utils.mappingDto.MappingPostDtoUtils;
import com.instaApp.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/posts")
public class PostController {
    private final PostService postService;
    private final ResponseErrorValidation errorValidation;
    private final MappingPostDtoUtils mappingPostDtoUtils;

    @Autowired
    public PostController(PostService postService, ResponseErrorValidation errorValidation, MappingPostDtoUtils mappingPostDtoUtils) {
        this.postService = postService;
        this.errorValidation = errorValidation;
        this.mappingPostDtoUtils = mappingPostDtoUtils;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> addPost(@Valid @RequestBody PostDTO postDTO,
                                          BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        PostEntity postEntity = postService.createPost(principal, postDTO);
        PostDTO createdPost = mappingPostDtoUtils.mapToPostDto(postEntity);
        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPost() {
        List<PostDTO> postDTOList = postService.findAllPost()
                .stream()
                .map(mappingPostDtoUtils::mapToPostDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostForUser(Principal principal) {
        List<PostDTO> postDTOList = postService.findPostForUser(principal)
                .stream()
                .map(mappingPostDtoUtils::mapToPostDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId
            , @PathVariable("username") String username) {
        System.out.println("loke leke leke; eke");
        PostEntity postEntity = postService.likePost(Long.parseLong(postId), username);
        PostDTO postDTO = mappingPostDtoUtils.mapToPostDto(postEntity);
        System.out.println(postDTO.toString());
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deleteById(Long.parseLong(postId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
