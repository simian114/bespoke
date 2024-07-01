package com.blog.bespoke.domain.repository;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(Long id);

    Post getById(Long id) throws RuntimeException;

    void delete(Post post);

    void deleteById(Long id);

    Page<Post> search(PostSearchCond cond);
}
