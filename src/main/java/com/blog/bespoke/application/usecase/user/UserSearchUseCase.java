package com.blog.bespoke.application.usecase.user;

import com.blog.bespoke.application.dto.request.userSearch.UserSearchRequestDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.dto.response.UserSearchResponseDto;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserSearchCond;
import com.blog.bespoke.domain.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchUseCase {
    private final UserRepository userRepository;

    @Transactional
    public UserSearchResponseDto userSearch(UserSearchRequestDto requestDto, User currentUser) {
        UserSearchCond cond = requestDto.toModel();
        return UserSearchResponseDto.from(userRepository.search(cond)
                .map(UserResponseDto::from));
    }
}
