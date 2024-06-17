package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.mapper.UserAppMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserUseCaseUnitTest {

    private final UserAppMapper userAppMapper = new UserAppMapper();
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setup() {
        userUseCase = new UserUseCase(userRepository, userAppMapper);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup_test() {
        // given
        UserSignupRequestDto requestDto = mock(UserSignupRequestDto.class);

        User mockUser = User.builder()
                .id(1L)
                .nickname("nickname")
                .password("password")
                .name("name")
                .email("email@gmail.com")
                .build();

        given(userRepository.save(Mockito.any()))
                .willReturn(mockUser);

        // when
        User user = userUseCase.signup(requestDto);

        // then
        assertThat(user.getEmail()).isEqualTo("email@gmail.com");
    }
}