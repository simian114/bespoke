package com.blog.bespoke.application.dto.request.userSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.UserSearchCond;

public interface UserSearchRequestDto {
    String toString();

    UserSearchCond toModel();
}
