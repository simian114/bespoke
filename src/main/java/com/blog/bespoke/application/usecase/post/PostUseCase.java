package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.mapper.PostRequestMapper;
import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.event.message.PublishPostEvent;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.Post;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.post.PostStatusCmd;
import com.blog.bespoke.domain.model.post.PostUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.post.PostRepository;
import com.blog.bespoke.domain.service.PostService;
import com.blog.bespoke.domain.service.UserCountInfoService;
import com.blog.bespoke.domain.service.UserService;
import com.blog.bespoke.domain.service.post.PostSearchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostUseCase {
    private final PostRepository postRepository;
    private final PostService postService;
    private final UserService userService;
    private final PostSearchService postSearchService;
    private final UserCountInfoService userCountInfoService;
    private final EventPublisher publisher;
    private final PostRequestMapper mapper = PostRequestMapper.INSTANCE;

    /**
     * 1. 내가 좋아요 했는지 여부
     * 2. 좋아요수
     * 3. 조회수
     */
    @Transactional
    public PostResponseDto showPostById(Long postId, User currentUser) {
        // TODO: postService 에서 get & viewcount 올리는 메서드 만들어야함
        Post post = postRepository.getById(postId);

        if (post.isDeleted()) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

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
        if (!postSearchService.canSearch(cond, currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        return postRepository.search(cond)
                .map(PostResponseDto::from);
    }

    @Transactional
    public PostResponseDto write(PostCreateRequestDto requestDto, User currentUser) {
        // user 의 상태를 검사해야하나?
        User author = userService.getById(currentUser.getId());
        Post post = mapper.toDomain(requestDto);
        post.init(author);
        if (requestDto.getStatus() != null) {
            post.changeStatus(requestDto.getStatus());
        }

        Post savedPost = postRepository.save(post);

        // post 의 상태가 published 인 경우에만!
        if (post.isPublished()) {
            publisher.publishPostPublishEvent(
                    PublishPostEvent.builder()
                            .postId(savedPost.getId())
                            .authorId(author.getId())
                            .title(savedPost.getTitle())
                            .description(savedPost.getDescription())
                            .build()
            );
        }

        return PostResponseDto.from(postRepository.save(post));
    }

    /**
     * DRAFT -> PUBLISHED 인 경우 이벤트 발생
     * * -> DRAFT 는 안된다.
     */
    @Transactional
    public PostResponseDto changeStatus(Long postId, PostStatusCmd cmd, User currentUser) {
        Post post = postRepository.getById(postId);
        if (!post.canUpdateBy(currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        if (!postService.canUpdateStatus(post, cmd.getStatus(), currentUser)) {
            throw new BusinessException(ErrorCode.POST_BAD_STATUS);
        }
        Post.Status asIs = post.getStatus();

        // NOTE: 기존 draft 에서 published 가 되면 이벤트 쏘기
        post.changeStatus(cmd.getStatus());
        if (asIs == Post.Status.DRAFT && cmd.getStatus() == Post.Status.PUBLISHED) {
            publisher.publishPostPublishEvent(
                    PublishPostEvent.builder()
                            .postId(post.getId())
                            .authorId(post.getAuthor().getId())
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .build()
            );
        }

        // NOTE: published 에서 다른 값으로 변경되면, published count 다운 해야함
        if (asIs == Post.Status.PUBLISHED && cmd.getStatus() != Post.Status.PUBLISHED) {
            userCountInfoService.decrementPublishedPostCount(post.getAuthor().getId());
        }

        return PostResponseDto.from(postRepository.save(post));
    }


    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateCmd postUpdateCmd, User currentUser) {
        Post post = postRepository.getById(postId);
        if (!post.canUpdateBy(currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        post.update(postUpdateCmd);
        return PostResponseDto.from(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId, User currentUser) {
        Post post = postRepository.getById(postId);
        if (!post.canUpdateBy(currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        post.delete();
        postRepository.save(post);
    }
}
