package com.instaApp.utils.mappingDto;

import com.instaApp.dto.PostDTO;
import com.instaApp.dto.UserDTO;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class MappingPostDtoUtils {

    public PostEntity mapToPostEntity(PostDTO postDTO, UserEntity userEntity) {
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setCaption(postDTO.getCaption());
        postEntity.setLikes(0L);
        postEntity.setTitle(postDTO.getTitle());
        postEntity.setLocation(postDTO.getLocation());
        return postEntity;
    }

    public PostDTO mapToPostDto(PostEntity postEntity) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(postEntity.getTitle());
        postDTO.setUsername(postEntity.getUser().getUsername());
        postDTO.setLocation(postEntity.getLocation());
        postDTO.setLikes(postEntity.getLikes());
        postDTO.setCaption(postEntity.getCaption());
        postDTO.setUsersLikes(postEntity.getLikedUsers());
        postDTO.setId(postEntity.getId());
        return postDTO;
    }
}
