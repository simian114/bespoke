package com.blog.bespoke.application.dto.request;

import lombok.Data;

@Data
public class CategoryCreateRequestDto {
    private String name;
    private String description;
    private String url;
}
