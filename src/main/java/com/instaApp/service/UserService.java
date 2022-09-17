package com.instaApp.service;

import com.instaApp.dto.UserDTO;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.payload.request.SignupRequest;

import java.security.Principal;

public interface UserService {


    public UserEntity createUser(SignupRequest userIn);
    public UserEntity updateUser(UserDTO userDTO, Principal principal);
}
