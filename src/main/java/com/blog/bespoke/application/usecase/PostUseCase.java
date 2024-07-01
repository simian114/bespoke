package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.mapper.PostRequestMapper;
import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.event.message.PostCreateMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.PostRepository;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.domain.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostUseCase {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final EventPublisher publisher;
    private final PostRequestMapper mapper = PostRequestMapper.INSTANCE;

    // 내가 좋아요 했는지 여부도 알 수 있음 좋겠다.
    @Transactional
    public PostResponseDto showPostById(Long postId, User currentUser) {
        Post post = postRepository.getById(postId);

        // check post's author is not banned
        User author = post.getAuthor();

        // throw 정지 된 유저의 게시글은 볼 수 없다.
        if (author.getBannedUntil() != null) {
            throw new BusinessException(ErrorCode.BANNED_USER_POST);
        }

        if (!postService.canShow(post, currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        return PostResponseDto.from(post);
    }

    @Transactional
    public Page<PostResponseDto> postSearch(PostSearchCond cond, User currentUser) {
        // published 가 아닌 정보를 요청하면서 user 가 null 인 경우는 예외를 보내야한다.
        if (cond.getStatus() != Post.Status.PUBLISHED && currentUser == null
                || cond.getStatus() != Post.Status.PUBLISHED && !Objects.equals(currentUser.getId(), cond.getAuthorId())) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        return postRepository.search(cond)
                .map(PostResponseDto::from);
    }

    @Transactional
    public PostResponseDto write(PostCreateRequestDto requestDto, User currentUser) {
        // user 의 상태를 검사해야하나?
        User author = userRepository.getById(currentUser.getId());
        Post post = mapper.toDomain(requestDto);
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);

        publisher.publishPostCreateEvent(
                PostCreateMessage.builder()
                        .postId(savedPost.getId())
                        .authorId(author.getId())
                        .title(savedPost.getTitle())
                        .description(savedPost.getDescription())
                        .build()
        );

        return PostResponseDto.from(postRepository.save(post));
    }
}
