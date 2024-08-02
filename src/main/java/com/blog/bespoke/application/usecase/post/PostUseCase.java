package com.blog.bespoke.application.usecase.post;

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
import com.blog.bespoke.domain.service.cache.PostCacheService;
import com.blog.bespoke.domain.service.post.PostS3ImageService;
import com.blog.bespoke.infrastructure.aws.s3.service.S3Service;
import com.blog.bespoke.infrastructure.repository.post.S3PostImageRepository;
import com.blog.bespoke.infrastructure.repository.redis.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EventPublisher publisher;
    private final S3Service s3Service;
    private final RedisUtil redisUtil;
    private final PostCacheService postCacheService;

    private String getPostDetailCacheKey(Long postId) {
        String POST_DETAIL_CACHE_KEY = "showPostById:";
        return POST_DETAIL_CACHE_KEY + postId;
    }

    private String getPostSearchCacheKey(PostSearchCond cond) {
        String POST_SEARCH_CACHE_KEY = "postSearch:";
        return POST_SEARCH_CACHE_KEY + cond.toString();
    }

    /**
     * 게시글 디테일 페이지
     * 누군가 좋아요 를 하면 캐싱 무효화
     * 1. 내가 좋아요 했는지 여부
     * 2. 좋아요수
     * 3. 조회수
     */
    @Transactional
    public PostResponseDto showPostById(Long postId, User currentUser) {
        PostResponseDto dto = redisUtil.get(getPostDetailCacheKey(postId), PostResponseDto.class);

        if (dto == null) {
            PostRelation relation = PostRelation.builder().count(true).author(true).category(true).build();
            dto = postRepository.findById(postId, relation)
                    .map(p -> PostResponseDto.from(p, relation))
                    .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
            // NOTE: 캐싱은 기본 한 시간
            redisUtil.set(getPostDetailCacheKey(postId), dto);
        }

        if (!dto.getStatus().equals(Post.Status.PUBLISHED)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        /*
         * TODO
         * 조건에 따른 조회수 증가 로직을 구현해야함. 그런에 redis 를 이용해 entity 가 아닌 dto 를 가져와서
         * post 의 비즈니스 메서드를 호출할 수 없음.
         * 아직 마땅한 해결방법을 찾지 못함. 해결 방법 찾은 이후 바로 수정할것.
         */
//         postService.getPostAndUpdateViewCountWhenNeeded(post, currentUser);

        return dto;
    }

    /**
     * 게시글 수정
     * - category join
     */
    @Transactional
    public PostResponseDto getPostForEdit(Long postId) {
        PostRelation relation = PostRelation.builder().category(true).cover(true).build();
        Post post = postRepository.getById(postId, relation);
        return PostResponseDto.from(post, relation);
    }

    @Transactional
    public PostResponseDto writePostAsTempSave(PostCreateRequestDto requestDto, User currentUser) {
        // user 의 상태를 검사해야하나?
        UserRelation relation = UserRelation.builder().categories(true).build();
        User author = userRepository.getById(currentUser.getId(), relation);
        Post post = requestDto.toModel();
        post.init(author);
        return PostResponseDto.from(postRepository.save(post));
    }

    /**
     * 상태는 PUBLISHED / HIDDEN 둘 중 하나로 변한다.
     * 이벤트
     * - DRAFT -> PUBLISHED 의 경우만 published 이벤트 발송. 이렇게 해야지만 published 에 대한 이벤트가 한번만 보내지는 걸로 보장할 수 있음
     * ---
     * 캐싱
     * - DRAFT -> HIDDEN 가 아닌 경우에 대해서 캐싱 invalidation
     */
    @Transactional
    public PostResponseDto changeStatus(Long postId, PostStatusCmd cmd, User currentUser) {
        PostRelation relation = PostRelation.builder().category(true).build();
        Post post = postRepository.getById(postId, relation);

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
        if (post.getCategory() != null) {
            postCacheService.invalidateBlogCategoryPosts(post.getCategory().getId());
        }

        return PostResponseDto.from(postRepository.save(post));
    }


    /**
     * 업데이트 성공 후에는 캐싱 무효화 해야함.
     */
    @Transactional
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, User currentUser) {
        User author = userRepository.getById(currentUser.getId(), UserRelation.builder().categories(true).build());
        PostRelation relation = PostRelation.builder().category(true).cover(true).build();
        Post post = postRepository.getById(postId, relation);
        if (!post.canUpdateBy(currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        // NOTE: 이미지 제거 이벤트를 날리고, 수신한 쪽에서 제거할까? 아니면 여기서 바로 제거할까? 뭐가됐던간에 비동기로 해야함.
        if (requestDto.getCover() != null) {
            S3PostImage cover = checkAndCreateS3PostImage(requestDto.getCover());
            cover.setPost(post);
            cover.setType(S3PostImage.Type.CONTENT);
            if (post.getCover() != null) {
                imageRepository.delete(post.getCover());
            }
            post.setCover(cover);
            imageRepository.save(cover);
        }

        Category category = author.getCategories().stream().filter(c -> c.getId().equals(requestDto.getCategoryId()))
                .findFirst().orElse(null);
        PostUpdateCmd cmd = new PostUpdateCmd(requestDto.getTitle(), requestDto.getDescription(), requestDto.getContent(), category, requestDto.getStatus());

        post.update(cmd);
        // 이미 publish 되어있으면 캐싱 제거
        // category 를 변경한 경우라면? 이전 & 이후 모두 캐시 제거해줘야함
        if (post.getStatus().equals(Post.Status.PUBLISHED) && post.getCategory() != null) {
            // 이전
            postCacheService.invalidateBlogCategoryPosts(post.getCategory().getId());
            // 이후
            if (!post.getCategory().getId().equals(cmd.getCategory().getId())) {
                postCacheService.invalidateBlogCategoryPosts(cmd.getCategory().getId());
            }
        }
        // NOTE: 캐싱 제거
        return PostResponseDto.from(postRepository.save(post), relation);
    }

    @Transactional
    public void deletePost(Long postId, User currentUser) {
        PostRelation relation = PostRelation.builder().category(true).build();
        Post post = postRepository.getById(postId, relation);
        if (!post.canUpdateBy(currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        post.delete();
        if (post.getStatus().equals(Post.Status.PUBLISHED) && post.getCategory() != null) {
            postCacheService.invalidateBlogCategoryPosts(post.getCategory().getId());
        }
        postRepository.save(post);
    }

    @Transactional
    public S3PostImageResponseDto uploadImage(MultipartFile file, S3PostImage.Type type, Long postId, User currentUser) {
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
        unSavedImage.setType(type);
        S3PostImage savedImage = imageRepository.save(unSavedImage);
        return S3PostImageResponseDto.from(savedImage);
    }

    private S3PostImage checkAndCreateS3PostImage(MultipartFile file) {

        postS3ImageService.checkCanUpload(file);

        S3PostImage unSavedImage;
        try {
            // s3 에 업로드 됨
            unSavedImage = s3Service.uploadFile(file);

        } catch (IOException e) {
            log.error("error..", e);
            throw new RuntimeException(e);
        }
        return unSavedImage;
    }
}
