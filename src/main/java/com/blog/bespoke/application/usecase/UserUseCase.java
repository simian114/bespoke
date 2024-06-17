package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.mapper.UserAppMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final UserAppMapper userAppMapper;

    // NOTE: @Valid 를 사용해도 동작하지 않음. 스프링은 기본적으로 Controller 에서만 Bean validation 이 진행됨
    public User signup(UserSignupRequestDto requestDto) {
        return userRepository.save(userAppMapper.toDomain(requestDto));
    }
}
