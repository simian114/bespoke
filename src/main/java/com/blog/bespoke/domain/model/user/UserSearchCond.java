package com.blog.bespoke.domain.model.user;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import com.blog.bespoke.domain.model.user.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSearchCond extends CommonSearchCond {
    private String email;
    private String nickname;
    private String name;

    private User.Status status;
    private Role.Code role;
}
