package com.blog.bespoke.application.usecase.user;

import com.blog.bespoke.application.dto.mapper.UserRequestMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.dto.request.UserUpdateRequestDto;
import com.blog.bespoke.application.dto.response.UserResponseDto;
import com.blog.bespoke.application.event.message.UserRegistrationMessage;
import com.blog.bespoke.application.event.publisher.EventPublisher;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.token.Token;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserProfile;
import com.blog.bespoke.domain.model.user.UserUpdateCmd;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.TokenRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.domain.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final EventPublisher eventPublisher;

    /**
     * 기본 회원가입. role 은 자동으로 USER 가 등록이됨. 어드민 유저는 DB 에서 직접 생성할것
     *
     * @param requestDto 회원가입에 필요한 정보
     * @return db 에 저장된 영속성 user 가 return 됨
     */
    @Transactional
    public UserResponseDto signup(UserSignupRequestDto requestDto) {
        User user = UserRequestMapper.INSTANCE.toDomain(requestDto);
        Role role = userRepository.getRoleByCode(Role.Code.USER);
        UserProfile profile = UserProfile.builder()
                .introduce(requestDto.getIntroduce())
                .build();
        user.setProfile(profile);
        userService.initUser(user, role);
        User savedUser = userRepository.save(user);

        Token token = tokenRepository.save(
                Token.builder()
                        .code(UUID.randomUUID().toString())
                        .refId(savedUser.getId())
                        .refType(Token.RefType.USER)
                        .type(Token.Type.EMAIL_VALIDATION)
                        .expiredAt(LocalDateTime.now().plusMinutes(3))
                        .build()
        );
        eventPublisher.publishMailEvent(
                UserRegistrationMessage.builder()
                        .email(user.getEmail())
                        .code(token.getCode())
                        .build()
        );
        return UserResponseDto.from(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto emailValidation(String code) {
        // 토큰 값을 이용해 Token 을 찾아옴
        Token token = tokenRepository.getByCode(code);
        // 토큰의 만료 기간을 체크함. 만료됐을 시 예외 throw 및 삭제
        if (token.hasExpired()) {
            tokenRepository.delete(token);
            throw new BusinessException(ErrorCode.TOKEN_HAS_EXPIRED);
        }
        User user = userRepository.getById(token.getRefId());
        user.activate();

        // 아직 만료가 되지 않았다면, token 에 있는 유저의 상태를 ACTIVE 로 변경함
        // 동시에 토큰을 삭제한다.
        return UserResponseDto.from(user);
    }

    public UserResponseDto getUserWithCategoryByNickname(String nickname) {
        return UserResponseDto.from(userRepository.getUserForPostCreateByNickname(nickname), com.blog.bespoke.application.dto.response.UserResponseDto.UserResponseDtoRelationUsage.builder().categories(true).build());
    }

    public UserResponseDto getUserByNickname(String nickname) {
        return UserResponseDto.from(userRepository.getByNickname(nickname));
    }


    public UserResponseDto getUserForPostWrite(String nickname) {
        return UserResponseDto.from(userRepository.getUserForPostCreateByNickname(nickname), com.blog.bespoke.application.dto.response.UserResponseDto.UserResponseDtoRelationUsage.builder().categories(true).build());
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto, Long userId) {
        // requestDto to userUpdateCmd
        UserUpdateCmd cmd = requestDto.toCmd();
        User user = userRepository.getById(userId);
        user.update(cmd);
        return UserResponseDto.from(user);
    }
}
