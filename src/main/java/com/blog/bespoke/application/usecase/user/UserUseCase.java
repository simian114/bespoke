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
import com.blog.bespoke.domain.model.user.*;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.model.user.role.UserRole;
import com.blog.bespoke.domain.repository.TokenRepository;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.domain.service.UserService;
import com.blog.bespoke.domain.service.user.UserS3ImageService;
import com.blog.bespoke.infrastructure.aws.s3.service.S3Service;
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
    private final UserS3ImageService userS3ImageService;
    private final S3Service s3Service;

    /**
     * 기본 회원가입. role 은 자동으로 USER 가 등록이됨. 어드민 유저는 DB 에서 직접 생성할것
     *
     * @param requestDto 회원가입에 필요한 정보
     * @return db 에 저장된 영속성 user 가 return 됨
     */
    @Transactional
    public UserResponseDto signup(UserSignupRequestDto requestDto) {
        /*
         * 아바타 업로드
         */
        if (requestDto.getAvatar() != null) {
            userS3ImageService.checkAvatarCanUpload(requestDto.getAvatar());
        }

        User user = UserRequestMapper.INSTANCE.toDomain(requestDto);
        Role role = userRepository.getRoleByCode(Role.Code.USER);
        UserProfile profile = UserProfile.builder()
                .introduce(requestDto.getIntroduce())
                .build();
        userService.initUser(user, role, profile);
        User savedUser = userRepository.save(user);

        if (requestDto.getAvatar() != null) {
            S3UserAvatar s3UserAvatar = s3Service.uploadAvatar(requestDto.getAvatar());
            savedUser.setAvatar(s3UserAvatar);
        }

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

    /**
     * 유저홈 페이지
     */
    public UserResponseDto getUserForUserHome(String nickname) {
        UserRelation relation = UserRelation.builder().profile(true).categories(true).build();
        return UserResponseDto.from(userRepository.getUserByNickname(nickname, relation), relation);
    }

    /**
     * user 의 정보 변경
     */
    public UserResponseDto getUserForProfileEdit(Long userId) {
        UserRelation relation = UserRelation.builder().profile(true).avatar(true).build();
        return UserResponseDto.from(userRepository.getById(userId, relation), relation);
    }


    /**
     * 게시글 생성 / 수정
     * - category 조인
     */
    public UserResponseDto getUserForPostWrite(Long userId) {
        UserRelation relation = UserRelation.builder().categories(true).build();
        return UserResponseDto.from(userRepository.getById(userId, relation), relation);
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto, Long userId) {
        // check file
        S3UserAvatar s3UserAvatar = null;
        if (requestDto.getAvatar() != null) {
            userS3ImageService.checkAvatarCanUpload(requestDto.getAvatar());
            s3UserAvatar = s3Service.uploadAvatar(requestDto.getAvatar());
        }
        // requestDto to userUpdateCmd
        UserUpdateCmd cmd = requestDto.toCmd(s3UserAvatar);
        User user = userRepository.getById(userId);
        user.update(cmd);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto userActivate(Long userId, User currentUser) {
        if (!currentUser.isAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        User user = userRepository.getById(userId);

        user.activate();
        return UserResponseDto.from(user);
    }

    @Transactional
    public void deleteUser(Long userId, User currentUser) {
        if (!currentUser.isAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserResponseDto bannedUntil(Long userId, Integer days, User currentUser) {
        if (!currentUser.isAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        User user = userRepository.getById(userId);

        LocalDateTime bannedUntilFromDays = userService.getBannedUntilFromDays(days);
        user.ban(bannedUntilFromDays);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto unban(Long userId, User currentUser) {
        if (!currentUser.isAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        User user = userRepository.getById(userId);

        user.unban();
        return UserResponseDto.from(user);
    }

    @Transactional
    public void requestAdvertiserRole(Long userId) {
        Token token = Token.builder()
                .refId(userId)
                .refType(Token.RefType.USER)
                .type(Token.Type.ADVERTISER_ROLE_REQUEST)
                .expiredAt(LocalDateTime.now().plusDays(3))
                .code(UUID.randomUUID().toString())
                .build();
        tokenRepository.save(token);
    }

    @Transactional
    public UserResponseDto addRole(Long userId, Role.Code code) {
        User user = userRepository.getById(userId);
        Role role = userRepository.getRoleByCode(code);
        tokenRepository.deleteAllByRefIdAndType(userId, Token.Type.ADVERTISER_ROLE_REQUEST);
        UserRole userRole = new UserRole(user.getId(), role.getId(), user, role);
        user.addRole(userRole);
        // TODO: 알림 주기
        return UserResponseDto.from(user);
    }
}

