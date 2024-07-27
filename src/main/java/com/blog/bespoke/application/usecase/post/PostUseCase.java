package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.mapper.PostRequestMapper;
import com.blog.bespoke.application.dto.request.PostCreateRequestDto;
import com.blog.bespoke.application.dto.request.PostUpdateRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.S3PostImageResponseDto;
import com.blog.bespoke.application.event.message.PublishPostEvent;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.post.*;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserRelation;
import com.blog.bespoke.domain.repository.post.PostRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.domain.service.PostService;
import com.blog.bespoke.domain.service.post.PostS3ImageService;
import com.blog.bespoke.domain.service.post.PostSearchService;
import com.blog.bespoke.infrastructure.aws.s3.service.S3Service;
import com.blog.bespoke.infrastructure.repository.post.S3PostImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostUseCase {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3PostImageRepository imageRepository;
    private final PostService postService;
    private final PostS3ImageService postS3ImageService;
    private final PostSearchService postSearchService;
    private final EventPublisher publisher;
    private final PostRequestMapper mapper = PostRequestMapper.INSTANCE;
    private final S3Service s3Service;

    /**
     * 게시글 디테일 페이지
     * 1. 내가 좋아요 했는지 여부
     * 2. 좋아요수
     * 3. 조회수
     */
    @Transactional
    public PostResponseDto showPostById(Long postId, User currentUser) {
        PostRelation relation = PostRelation.builder().count(true).author(true).category(true).build();

        Post post = postRepository.findById(postId, relation)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        boolean likedByUser = currentUser != null && postRepository.existsPostLikeByPostIdAndUserId(post.getId(), currentUser.getId());

        postService.getPostAndUpdateViewCountWhenNeeded(post, likedByUser, currentUser);
        return PostResponseDto.from(post, relation);
    }

    /**
     * 게시글 수정
     * - category join
     */
    public PostResponseDto getPostForEdit(Long postId) {
        PostRelation relation = PostRelation.builder().category(true).build();
        return PostResponseDto.from(postRepository.getById(postId, relation), relation);
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
        UserRelation relation = UserRelation.builder().categories(true).build();
        User author = userRepository.getById(currentUser.getId(), relation);
        Post post = mapper.toDomain(requestDto);
        Category category = author.getCategories().stream().filter(c -> c.getId().equals(requestDto.getCategoryId()))
                .findFirst().orElse(null);
        post.init(author);
        post.setCategory(category);

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
            userRepository.decrementPublishedPostCount(post.getAuthor().getId());
        }

        return PostResponseDto.from(postRepository.save(post));
    }


    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser) {
        User author = userRepository.getById(currentUser.getId(), UserRelation.builder().categories(true).build());
        //User author = userRepository.getUserWithCategories(currentUser.getId());
        Post post = postRepository.getById(postId);
        if (!post.canUpdateBy(currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        Category category = author.getCategories().stream().filter(c -> c.getId().equals(requestDto.getCategoryId()))
                .findFirst().orElse(null);
        PostUpdateCmd cmd = new PostUpdateCmd(requestDto.getTitle(), requestDto.getDescription(), requestDto.getContent(), category);
        post.update(cmd);
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

    @Transactional
    public S3PostImageResponseDto uploadImage(MultipartFile file, Long postId, User currentUser) {
        // NOTE: 이 과정을 없애고 싶은데 방법이 없을까?
        Post post = postRepository.getById(postId, PostRelation.builder().author(true).build());
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        postS3ImageService.checkCanUpload(file);

        S3PostImage unSavedImage;
        try {
            // s3 에 업로드 됨
            unSavedImage = s3Service.uploadFile(file);

        } catch (IOException e) {
            log.error("error..", e);
            throw new RuntimeException(e);
        }
        unSavedImage.setPost(Post.builder().id(postId).build());
        S3PostImage savedImage = imageRepository.save(unSavedImage);
        return S3PostImageResponseDto.from(savedImage);
    }
}
