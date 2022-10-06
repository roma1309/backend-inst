package com.instaApp.web.controller;

import com.instaApp.dto.UserDTO;
import com.instaApp.model.entity.UserEntity;
import com.instaApp.service.UserService;
import com.instaApp.utils.mappingDto.MappingUserDtoUtils;
import com.instaApp.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final MappingUserDtoUtils mappingUserDtoUtils;
    private final ResponseErrorValidation errorValidation;

    @Autowired
    public UserController(UserService userService, MappingUserDtoUtils mappingUserDtoUtils, ResponseErrorValidation errorValidation) {
        this.userService = userService;
        this.mappingUserDtoUtils = mappingUserDtoUtils;
        this.errorValidation = errorValidation;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        UserEntity userEntity = userService.getCurrentUser(principal);
        UserDTO userDTO = mappingUserDtoUtils.mapToUserDto(userEntity);
        return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        UserEntity userEntity = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = mappingUserDtoUtils.mapToUserDto(userEntity);
        return new ResponseEntity(userDTO, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> putUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        UserEntity userEntity = userService.updateUser(userDTO, principal);
        UserDTO userUpdateDto = mappingUserDtoUtils.mapToUserDto(userEntity);
        return new ResponseEntity<>(userUpdateDto, HttpStatus.OK);
    }
}
