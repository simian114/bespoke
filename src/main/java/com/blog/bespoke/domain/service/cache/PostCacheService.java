package com.blog.bespoke.domain.service.cache;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import lombok.RequiredArgsConstructor;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
public class PostCacheService {

    public static String DETAIL_POST = "postDetail:%d";

    /**
     * 블로그홈
     * 한 시간 캐싱.
     * invalidation 은 하지 않음
     * 1. %s: nickname
     */
    public static String SEARCH_BLOG_HOME_POSTS_KEY = "p:s:blog-home:%s";

    /**
     * 아래의 경우 캐싱 invalidation
     * - 생성
     * - 삭제
     * - 수정 (상태 변경)
     * 1. %d: category
     * 2. %d: page
     */
    public static String SEARCH_BLOG_POSTS_KEY = "p:s:blog:%d:%d";
    public static String SEARCH_BLOG_POSTS_INVALIDATION_KEY = "p:s:blog:%d:*";

    /**
     * 메인홈 post search list 는 캐싱 무효화를 직접하지 않음
     */
    public static String SEARCH_MAIN_POSTS = "p:s:main:%s:%d";


    public boolean useMemoryCache(PostSearchCond cond) {
        return !TRUE.equals(cond.getManage());
    }


    public String getPostSearchCacheKey(PostSearchCond cond, PostSearchCacheType type) {
        switch (type) {
            case BLOG -> {
                return String.format(SEARCH_BLOG_POSTS_KEY, cond.getCategory(), cond.getPage());
            }
            case MAIN -> {
                return String.format(SEARCH_MAIN_POSTS, cond.getOrderBy().name(), cond.getPage());
            }
            case BLOG_HOME -> {
                return String.format(SEARCH_BLOG_HOME_POSTS_KEY, cond.getNickname());
            }
        }
        return "postSearch";
    }

    /**
     * invalidate
     */

    public enum PostSearchCacheType {
        MAIN,
        BLOG_HOME,
        BLOG,
    }
}
