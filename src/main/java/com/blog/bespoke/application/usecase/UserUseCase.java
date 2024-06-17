package com.blog.bespoke.application.usecase;

import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserService userService;
    private final UserRepository userRepository;
}
