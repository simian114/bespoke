package com.blog.bespoke.domain.model.token;

import com.blog.bespoke.domain.model.common.CommonSearchCond;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TokenSearchCond extends CommonSearchCond {
    private List<Token.Type> types;
}

