package com.instaApp.utils.mappingDto;

import com.instaApp.dto.UserDTO;
import com.instaApp.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class MappingUserDtoUtils {

    public UserEntity mapToUserEntity(UserDTO userDTO, UserEntity userEntity) {
       // System.out.println(userDTO.toString());
        userEntity.setName(userDTO.getFirstname());
        userEntity.setLastname(userDTO.getLastname());
        userEntity.setBio(userDTO.getBio());
        userEntity.setAge(userDTO.getAge());
        userEntity.setUsername(userDTO.getUsername());
        return userEntity;
    }

    public UserDTO mapToUserDto(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();

        userDTO.setAge(userEntity.getAge());
        userDTO.setBio(userEntity.getBio());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setFirstname(userEntity.getName());
        userDTO.setLastname(userEntity.getLastname());
        return userDTO;
    }
}
