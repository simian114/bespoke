package com.blog.bespoke.application.dto.request;

import com.blog.bespoke.domain.model.category.Category;
import lombok.Data;

@Data
public class CategoryCreateRequestDto {
    private String name;
    private String description;
    private String url;
    private int priority;

    public Category toModel() {
        return Category.builder()
                .name(name)
                .description(description)
                .url(url)
                .priority(priority)
                .visible(true)
                .build();
    }

}
