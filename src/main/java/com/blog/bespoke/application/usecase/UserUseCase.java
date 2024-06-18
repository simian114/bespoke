package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.mapper.UserAppMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final UserAppMapper userAppMapper;
    // NOTE: passwordEncoder 는 infrastructure 에서 제공해야한다.
    /*
    생각해볼것.. passwordEncoder 를 제공하는 것보다는 repository 에 signup 이라는 메서드를 만드는게 좋지 않을까?
     */
    private final PasswordEncoder passwordEncoder;

    // NOTE: @Valid 를 사용해도 동작하지 않음. 스프링은 기본적으로 Controller 에서만 Bean validation 이 진행됨
    public User signup(UserSignupRequestDto requestDto) {
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userRepository.save(userAppMapper.toDomain(requestDto));
    }
}
