package com.blog.bespoke.domain.model.common;

import lombok.Getter;
import lombok.Setter;

/**
 * pageSize: default 값: 10
 * offset: 일반적인 페이지 (테이블 등)
 * cursor: 무한스크롤에 사용. id 기반으로 한다.
 */
@Getter
@Setter
public class CommonSearchCond {
    protected Integer pageSize = 10;

    // protected Long cursor;
    protected Integer page = 0;
}
