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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSearchUseCase {
    private final PostSearchService postSearchService;
    private final PostRepository postRepository;

    /**
     * 캐싱
     * 1. redis 캐시에서 찾음
     * 2. 캐시에 존재하면 바로 return
     * 3. 캐시에 존재하지 않으면 postSearch 를 이용해 데이터 가져옴
     * ❗어떤 경우에는 캐싱을 하면 안됨. -> manage 가 true 인 경우
     * ---
     * 캐싱 invalidation
     * ---
     * relation
     * list 에는 일단 공식적으로 relation 이 적용되지 않음.
     * 왜냐면 list 요청에는 최소한의 정보만을 가져오기 때문임.
     * 따라서 relation 의 역할은, entity -> dto 의 변환 과정에 관여하기 위함.
     */
    @Transactional
    public PostSearchResponseDto postSearch(PostSearchRequestDto requestDto, User currentUser) {
        PostSearchCond cond = requestDto.toModel();
        PostRelation postRelation = PostRelation.builder().cover(true).author(true).build();

        if (!postSearchService.canSearch(cond, currentUser)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        return PostSearchResponseDto.from(postRepository.search(cond)
                .map(post -> PostResponseDto.from(post, postRelation)));
    }

}
