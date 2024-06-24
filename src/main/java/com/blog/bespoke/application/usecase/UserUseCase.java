package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.mapper.UserAppMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.domain.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserAppMapper userAppMapper;

    /**
     * 기본 회원가입. role 은 자동으로 USER 가 등록이됨. 어드민 유저는 DB 에서 직접 생성할것
     *
     * @param requestDto 회원가입에 필요한 정보
     * @return db 에 저장된 영속성 user 가 return 됨
     */
    @Transactional
    public User signup(UserSignupRequestDto requestDto) {
        User user = userAppMapper.toDomain(requestDto);
        user.changePassword(userService.encodePassword(user.getPassword()));
        userService.addRole(user, Role.CODE.USER);

        return userRepository.save(user);
    }
}
