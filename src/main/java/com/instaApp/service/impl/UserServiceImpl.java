package com.instaApp.service.impl;

import com.instaApp.dto.UserDTO;
import com.instaApp.exceptions.UserExistException;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.model.enums.Role;
import com.instaApp.payload.request.SignupRequest;
import com.instaApp.repository.UserRepo;
import com.instaApp.security.JWTTokenProvider;
import com.instaApp.service.UserService;
import com.instaApp.utils.mappingDto.MappingUserDtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepo userRepo;
    private final MappingUserDtoUtils mappingUserDtoUtils;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepo userRepo, MappingUserDtoUtils mappingUserDtoUtils) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
        this.mappingUserDtoUtils = mappingUserDtoUtils;
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

    @Override
    public UserEntity updateUser(UserDTO userDTO, Principal principal) {
        return userRepo.save(mappingUserDtoUtils.mapToUserEntity(userDTO, getUserByPrincipal(principal)));
    }

    @Override
    public UserEntity getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    @Override
    public UserEntity getUserById(long userId) {
        UserEntity userEntity = userRepo.findById(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return userEntity;
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        UserEntity userEntity = userRepo.findByEmail(username);
        if (userEntity == null) {
            new UsernameNotFoundException("Username not found with username - " + username);
        }
        return userEntity;
    }
}
