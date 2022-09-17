package com.instaApp.utils.mappingDto;

import com.instaApp.dto.UserDTO;
import com.instaApp.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class MappingUserDtoUtils {

    public UserDTO mapToUserDto(UserEntity userEntity) {
        return null;
    }

    public UserEntity mapToUserEntity(UserDTO userDTO, UserEntity userEntity) {
        userEntity.setName(userDTO.getFirstname());
        userEntity.setLastname(userDTO.getLastname());
        userEntity.setBio(userDTO.getBio());
        userEntity.setAge(userDTO.getAge());
        userEntity.setUsername(userDTO.getUsername());
        return userEntity;
    }
}
