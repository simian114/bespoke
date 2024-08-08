package com.blog.bespoke.domain.model.user;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import com.blog.bespoke.domain.model.user.role.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSearchCond extends CommonSearchCond {
    User.Status status;
    Role.Code role;
}
