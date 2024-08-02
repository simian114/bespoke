package com.blog.bespoke.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostSearchResponseDto {
    private List<PostResponseDto> content;
    private int page;
    private int totalPage;
    private long totalElements;
    private boolean nextPage;
    private boolean prevPage;


    public boolean hasNext() {
        return nextPage;
    }

    public boolean hasPrevious() {
        return prevPage;
    }

    public boolean isEmpty() {
        return totalElements == 0;
    }

    static public PostSearchResponseDto from(Page<PostResponseDto> model) {
        return PostSearchResponseDto.builder()
                .content(model.getContent())
                .page(model.getPageable().getPageNumber())
                .totalPage(model.getTotalPages())
                .totalElements(model.getTotalElements())
                .nextPage(model.hasNext())
                .prevPage(model.hasPrevious())
                .build();
    }
}

