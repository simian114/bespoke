package com.blog.bespoke.domain.repository.post;

import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostRelation;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostRepository extends PostCountInfoRepository,
        PostLikeRepository
{
    Post save(Post post);

    Optional<Post> findById(Long id);

    Optional<Post> findById(Long id, PostRelation relation);

    Post getById(Long id) throws RuntimeException;
    Post getById(Long id, PostRelation relation) throws RuntimeException;

    void delete(Post post);

    void deleteById(Long id);

    Page<Post> search(PostSearchCond cond);

    Optional<Post> findPostWithLikeByPostIdAndUserId(Long postId, Long userId);
    Post getPostWithLikeByPostIdAndUserId(Long postId, Long userId);
}
