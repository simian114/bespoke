package com.blog.bespoke.domain.repository;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(Long id);

    Post getById(Long id) throws RuntimeException;

    void delete(Post post);

    void deleteById(Long id);

    Page<Post> search(PostSearchCond cond, Pageable pageable);
}
