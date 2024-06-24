package com.blog.bespoke.infrastructure.security;

import com.blog.bespoke.application.dto.mapper.UserAppMapper;
import com.blog.bespoke.application.dto.request.UserSignupRequestDto;
import com.blog.bespoke.application.usecase.UserUseCase;
import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.infrastructure.repository.config.RepositoryConfig;
import com.blog.bespoke.infrastructure.security.config.PasswordEncoderConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                RepositoryConfig.class,
                UserDetailsService.class,
                UserUseCase.class,
                UserAppMapper.class,
                PasswordEncoderConfig.class
        })
)
@Transactional
public class UserDetailsServiceImplTest {
    @Autowired
    private UserUseCase userUseCase;
    @Autowired
    private UserDetailsService userDetailsService;


    @Test
    @DisplayName("User details 로 유저 찾기")
    void test() {
        // given
        User user = userUseCase.signup(UserSignupRequestDto.builder().email("email@gmail.com").password("password").nickname("nickname").name("name").build());

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // then
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 email 이면 UsernameNotFoundException 발생")
    void notfound_test() {
        // given & when & then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("not exist email")
        );
    }
}
