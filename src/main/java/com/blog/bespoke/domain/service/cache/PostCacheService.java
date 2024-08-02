package com.blog.bespoke.domain.service.cache;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.infrastructure.repository.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class PostCacheService {
    public static String DETAIL_POST = "postDetail:%d";

    /**
     * 아래의 경우 캐싱 invalidation
     * - 생성
     * - 삭제
     * - 수정 (상태 변경)
     * 1. %d: category
     * 2. %d: page
     */
    public static String SEARCH_BLOG_POSTS_KEY = "postSearch:blog:%d:%d";
    public static String SEARCH_BLOG_POSTS_INVALIDATION_KEY = "postSearch:blog:%d:*";

    /**
     * 메인홈 post search list 는 캐싱 무효화를 직접하지 않음
     */
    public static String SEARCH_MAIN_POSTS = "postSearch:main:%s:%d";
    // public static String SEARCH_MAIN_POSTS_INVALIDATION_KEY = "postSearch:main:%s:*";



    public boolean useMemoryCache(PostSearchCond cond) {
        return !TRUE.equals(cond.getManage());
    }


    private final RedisUtil redisUtil;

    public String getPostSearchCacheKey(PostSearchCond cond, PostSearchCacheType type) {
        switch (type) {
            case BLOG -> {
                return String.format(SEARCH_BLOG_POSTS_KEY, cond.getCategory(), cond.getPage());
            }
            case MAIN -> {
                return String.format(SEARCH_MAIN_POSTS, cond.getOrderBy().name(), cond.getPage());
            }
        }
        return "postSearch";
    }

    public void invalidateBlogCategoryPosts(Long categoryId) {
        redisUtil.invalidateByPattern(String.format(SEARCH_BLOG_POSTS_INVALIDATION_KEY, categoryId));
    }

    public enum PostSearchCacheType {
        MAIN,
        BLOG
    }
}
