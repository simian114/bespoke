package com.blog.bespoke.application.dto.response.search;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonSearchResponseDto<T> {
    protected List<T> content;
    protected int page;
    protected int totalPage;
    protected long totalElements;
    protected boolean nextPage;
    protected boolean prevPage;

    public boolean hasNext() {
        return nextPage;
    }

    public boolean hasPrevious() {
        return prevPage;
    }

    public boolean isEmpty() {
        return totalElements == 0;
    }

    public CommonSearchResponseDto<T> from(Page<T> model) {
        return new CommonSearchResponseDto<>(
                model.getContent(),
                model.getPageable().getPageNumber(),
                model.getTotalPages(),
                model.getTotalElements(),
                model.hasNext(),
                model.hasPrevious()
        );
    }

}
