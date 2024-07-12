package com.blog.bespoke.domain.repository.user;

import com.blog.bespoke.domain.model.user.User;

import java.util.Optional;

public interface UserCategoryRepository {
    // save 는 user 로 할거기 때문에 필요없음
    Optional<User> findUserWithCategories(Long userId);

    User getUserWithCategories(Long userId);

    Optional<User> findUserForPostCreateByNickname(String nickname);

    User getUserForPostCreateByNickname(String nickname);
}
