package com.blog.bespoke.domain.model.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * pageSize: default 값: 10
 * offset: 일반적인 페이지 (테이블 등)
 * cursor: 무한스크롤에 사용. id 기반으로 한다.
 */
@Getter
@Setter
public class CommonSearchCond {
    protected Integer pageSize = 20;

    // protected Long cursor;
    protected Integer page = 0;

    protected boolean count = true;

    public Pageable getPageable() {
        return PageRequest.of(page == null ? 0 : page, pageSize == null ? 20 : pageSize);
    }
}
