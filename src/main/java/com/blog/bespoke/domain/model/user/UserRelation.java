package com.blog.bespoke.domain.model.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRelation {
    private boolean categories;
    private boolean profile;
    private boolean count;
    private boolean roles;
}
