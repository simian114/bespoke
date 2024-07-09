package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.domain.model.post.S3PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3PostImageRepository extends JpaRepository<S3PostImage, Long> {
}
