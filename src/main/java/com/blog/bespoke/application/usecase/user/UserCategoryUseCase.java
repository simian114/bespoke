package com.blog.bespoke.application.usecase.user;

import com.blog.bespoke.application.dto.mapper.CategoryRequestMapper;
import com.blog.bespoke.application.dto.request.CategoryCreateRequestDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.category.Category;
import com.blog.bespoke.domain.model.user.CategoryUpdateCmd;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.domain.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCategoryUseCase {
    private final UserRepository userRepository;
    private final UserCategoryService userCategoryService;

    public User getUserWithCategory(Long userId) {
        User user = userRepository.getUserWithCategories(userId);
        return user;
    }

    @Transactional
    public User createCategory(CategoryCreateRequestDto requestDto, User currentUser) {
        User user = userRepository.getUserWithCategories(currentUser.getId());
        Category category = CategoryRequestMapper.INSTANCE.toDomain(requestDto);

        if (!userCategoryService.canMakeCategory(user, category)) {
            throw new BusinessException(ErrorCode.NO_MORE_CATEGORY);
        }

        user.addCategory(category);
        return user;
    }

    @Transactional
    public User updateCategory(Long categoryId, CategoryUpdateCmd cmd, User currentUser) {
        User user = userRepository.getUserWithCategories(currentUser.getId());

        Category category = user.categories.stream().filter(c -> c.getId().equals(categoryId))
                .findFirst().orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        category.update(cmd);
        return user;
    }

    @Transactional
    public User deleteCategory(Long categoryId, User currentUser) {
        User user = userRepository.getUserWithCategories(currentUser.getId());
        user.removeCategory(categoryId);
        return user;
    }


}
