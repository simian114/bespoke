package com.blog.bespoke.application.usecase.post;

import com.blog.bespoke.application.dto.request.postSearch.PostSearchRequestDto;
import com.blog.bespoke.application.dto.response.PostResponseDto;
import com.blog.bespoke.application.dto.response.PostSearchResponseDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.post.PostRelation;
import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.post.PostRepository;
import com.blog.bespoke.domain.service.post.PostSearchService;
import com.blog.bespoke.infrastructure.repository.redis.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSearchUseCase {
    private static String CACHE_KEY_PREFIX = "postSearch:";
    private final RedisUtil redisUtil;
    private final PostSearchService postSearchService;
    private final PostRepository postRepository;

    /**
     * 1. redis 캐시에서 찾음
     * 2. 캐시에 존재하면 바로 return
     * 3. 캐시에 존재하지 않으면 postSearch 를 이용해 데이터 가져옴
     */
    @Transactional
    public PostSearchResponseDto postSearch(PostSearchRequestDto requestDto, User currentUser) {
        PostSearchCond cond = requestDto.toModel();
        if (!postSearchService.canSearch(cond, currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }
        PostSearchResponseDto cached = redisUtil.get(CACHE_KEY_PREFIX + requestDto, PostSearchResponseDto.class);
        if (cached != null) {
            return cached;
        }
        PostSearchResponseDto res = PostSearchResponseDto.from(postRepository.search(cond)
                .map(post -> PostResponseDto.from(post, PostRelation.builder().cover(true).build())));
        redisUtil.set(CACHE_KEY_PREFIX + requestDto, res);
        return res;
    }

}
