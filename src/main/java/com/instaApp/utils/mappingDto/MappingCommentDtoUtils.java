package com.instaApp.utils.mappingDto;

import com.instaApp.dto.CommentDTO;
import com.instaApp.dto.PostDTO;
import com.instaApp.model.entity.CommentEntity;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class MappingCommentDtoUtils {

    public CommentEntity mapToCommentEntity(CommentDTO commentDTO,
                                            PostEntity postEntity, UserEntity userEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPost(postEntity);
        commentEntity.setMessage(commentDTO.getMessage());
        commentEntity.setUserId(userEntity.getId());
        commentEntity.setUsername(userEntity.getUsername());
        return commentEntity;
    }

    public CommentDTO mapToCommentDto(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setMessage(commentEntity.getMessage());
        commentDTO.setUsername(commentEntity.getUsername());
        commentDTO.setId(commentEntity.getId());
        return commentDTO;
    }
}
