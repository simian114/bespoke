package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.mapper.UserAppMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseUnitTest {
    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAppMapper userAppMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setup() {
        // TODO: password encoder 넣고 테스트 해야함
        userUseCase = new UserUseCase(userRepository, userService, userAppMapper);
    }

    @Test
    @DisplayName("회원가입 시 password 를 encoding 해야함")
    void signup_password_encoding_test() {
        // given
        UserSignupRequestDto requestDto = mock(UserSignupRequestDto.class);
        given(userRepository.save(Mockito.any()))
                .willReturn(mock(User.class));
        given(requestDto.getPassword())
                .willReturn("hello");

        // when
        userUseCase.signup(requestDto);

        // then
        verify(passwordEncoder, times(1))
                .encode(anyString());
    }
}