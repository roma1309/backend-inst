package com.instaApp.repository;

import com.instaApp.model.entity.PostEntity;
import com.instaApp.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByUserOrderByCreatedDateDesc(UserEntity user);

    List<PostEntity> findByOrderByCreatedDateDesc();

    PostEntity findByIdAndUser(Long id, UserEntity user);

    void delete(PostEntity postEntity);
}
