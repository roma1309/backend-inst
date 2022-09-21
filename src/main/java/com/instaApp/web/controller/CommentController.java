package com.instaApp.web.controller;

import com.instaApp.dto.CommentDTO;
import com.instaApp.model.entity.CommentEntity;
import com.instaApp.payload.response.MessageResponse;
import com.instaApp.service.CommentService;
import com.instaApp.utils.mappingDto.MappingCommentDtoUtils;
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
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;
    private final ResponseErrorValidation errorValidation;
    private final MappingCommentDtoUtils mappingCommentDtoUtils;

    @Autowired
    public CommentController(CommentService commentService, ResponseErrorValidation errorValidation, MappingCommentDtoUtils mappingCommentDtoUtils) {
        this.commentService = commentService;
        this.errorValidation = errorValidation;
        this.mappingCommentDtoUtils = mappingCommentDtoUtils;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> postComment(@Valid @RequestBody CommentDTO commentDTO
            , @PathVariable("postId") String postId
            , BindingResult bindingResult
            , Principal principal) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        CommentEntity commentEntity = commentService.createComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO createCommentDto = mappingCommentDtoUtils.mapToCommentDto(commentEntity);
        return new ResponseEntity<>(createCommentDto, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        List<CommentDTO> commentDTOList = commentService.findAllCommentForPost(Long.parseLong(postId))
                .stream()
                .map(mappingCommentDtoUtils::mapToCommentDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }
}
