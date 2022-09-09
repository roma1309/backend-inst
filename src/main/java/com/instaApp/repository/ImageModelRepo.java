package com.instaApp.repository;

import com.instaApp.model.entity.ImageModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageModelRepo extends JpaRepository<ImageModelEntity, Long> {

    ImageModelEntity findByUserId(Long id);

    ImageModelEntity findByPostId(Long id);

}
