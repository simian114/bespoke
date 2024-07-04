package com.blog.bespoke.domain.repository.post;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostRepository extends PostCountInfoRepository,
        PostLikeRepository
{
    Post save(Post post);

    Optional<Post> findById(Long id);

    Post getById(Long id) throws RuntimeException;

    void delete(Post post);

    void deleteById(Long id);

    Page<Post> search(PostSearchCond cond);

    Optional<Post> findPostWithLikeByPostIdAndUserId(Long postId, Long userId);
    Post getPostWithLikeByPostIdAndUserId(Long postId, Long userId);
}
