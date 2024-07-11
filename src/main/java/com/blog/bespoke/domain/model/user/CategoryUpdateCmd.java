package com.blog.bespoke.domain.model.user;

import lombok.Data;

@Data
public class CategoryUpdateCmd {
    private String name;
    private String description;
}
