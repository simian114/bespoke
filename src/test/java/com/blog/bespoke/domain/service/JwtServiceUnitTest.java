package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class JwtServiceUnitTest {
    private final JwtService jwtService = new JwtService();

    // NOTE: reflection 을 이용한 테스트
    @BeforeEach
    void init() {
        // Mockito 로 하는 방법은 없을까?
        String jwtSecretKey = "7Iqk7YyM66W07YOA7L2U65Sp7YG065+9U3ByaW5n6rCV7J2Y7Yqc7YSw7LWc7JuQ67mI7J6F64uI64ukLg==";
        ReflectionTestUtils.setField(jwtService, "jwtSecretKey", jwtSecretKey);
        ReflectionTestUtils.invokeMethod(jwtService, "init");
    }

    @Test
    @DisplayName("access token 발급 테스트")
    void create_access_token_test() {
        // given
        User user = User.builder()
                .id(1L)
                .name("name")
                .nickname("nickname")
                .password("password")
                .email("email@gmail.com").build();

        // when
        String accessToken = jwtService.createAccessToken(user);

        // then
        assertThat(accessToken).isNotEmpty();
    }

    @Test
    @DisplayName("parse access token 테스트")
    void parseAccessTokenToUserTest() {
        // given
        User user = User.builder()
                .id(1L)
                .name("name")
                .nickname("nickname")
                .email("email@gmail.com")
                .build();
        String accessToken = jwtService.createAccessToken(user);

        // when
        User userFromToken = jwtService.parseAccessTokenToUser(accessToken);

        // then
        assertThat(userFromToken.getId()).isEqualTo(user.getId());
        assertThat(userFromToken.getEmail()).isEqualTo(user.getEmail());
        assertThat(userFromToken.getName()).isEqualTo(user.getName());
        assertThat(userFromToken.getNickname()).isEqualTo(user.getNickname());
    }
}