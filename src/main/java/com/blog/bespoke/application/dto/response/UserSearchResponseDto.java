package com.blog.bespoke.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
public class UserSearchResponseDto {
    private List<UserResponseDto> content;
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

    static public UserSearchResponseDto from(Page<UserResponseDto> model) {
        return UserSearchResponseDto.builder()
                .content(model.getContent())
                .page(model.getPageable().getPageNumber())
                .totalPage(model.getTotalPages())
                .totalElements(model.getTotalElements())
                .nextPage(model.hasNext())
                .prevPage(model.hasPrevious())
                .build();
    }

}
