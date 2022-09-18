package com.instaApp.utils.mappingDto;

import com.instaApp.dto.PostDTO;
import com.instaApp.dto.UserDTO;
import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
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
}
