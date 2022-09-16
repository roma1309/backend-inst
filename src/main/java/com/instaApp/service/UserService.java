package com.instaApp.service;

import com.instaApp.model.entity.UserEntity;
import com.instaApp.payload.request.SignupRequest;

public interface UserService {


    public UserEntity createUser(SignupRequest userIn);

}
