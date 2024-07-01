package com.blog.bespoke.infrastructure.repository.post;

import com.blog.bespoke.domain.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
}
