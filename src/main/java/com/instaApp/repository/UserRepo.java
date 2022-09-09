package com.instaApp.repository;

import ch.qos.logback.core.Layout;
import com.instaApp.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<UserEntity, Layout> {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    UserEntity findById(Long id);
}
