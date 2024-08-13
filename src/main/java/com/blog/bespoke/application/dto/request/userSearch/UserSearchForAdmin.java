package com.blog.bespoke.application.dto.request.userSearch;

import com.blog.bespoke.domain.model.post.PostSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserSearchCond;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchForAdmin implements UserSearchRequestDto {
    private String email;
    private String nickname;
    private String name;
    private User.Status status;
    private Integer page;

    @Override
    public UserSearchCond toModel() {
        UserSearchCond cond = new UserSearchCond();
        cond.setEmail(email);
        cond.setNickname(nickname);
        cond.setName(name);
        cond.setStatus(status);
        cond.setPage(page);
        return cond;
    }
}
