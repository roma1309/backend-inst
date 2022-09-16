package com.instaApp.service.impl;

import com.instaApp.exceptions.UserExistException;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.model.enums.Role;
import com.instaApp.payload.request.SignupRequest;
import com.instaApp.repository.UserRepo;
import com.instaApp.security.JWTTokenProvider;
import com.instaApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepo userRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public UserEntity createUser(SignupRequest userIn) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userIn.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        userEntity.setUsername(userIn.getUsername());
        userEntity.setLastname(userIn.getLastname());
        userEntity.setName(userIn.getFirstname());

        userEntity.getRoles().add(Role.ROLE_USER);

        try {
            LOG.info("Saving user {}", userIn.getEmail());
            return userRepo.save(userEntity);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException("The user" + userIn.getUsername() + "already exist. Please check credentials");
        }
    }
}
