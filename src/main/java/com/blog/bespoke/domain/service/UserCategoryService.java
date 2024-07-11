package com.blog.bespoke.domain.service;

import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserCategoryService {

    /**
     * user: userWithCategories.
     * categories 가 null 이면 return false
     * 1. category 는 최대 3개까지 만들 수 있다.
     * 2. category 의 이름은 중복될 수 없다.
     */
    public boolean canMakeCategory(User user, Category category) {
        if (user.categories == null || user.categories.size() >= 3) {
            throw new BusinessException(ErrorCode.NO_MORE_CATEGORY);
        }
        if (user.categories.stream().anyMatch(c -> c.getName().equals(category.getName()))) {
            throw new BusinessException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }

        return true;
    }
}
