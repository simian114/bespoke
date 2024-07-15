package com.blog.bespoke.domain.model.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateCmd {
    private String name;
    private String introduce;
}
