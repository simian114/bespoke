package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("role 추가 테스트")
    void add_role_test() {
        // given
        User user = User.builder().id(1L).email("email@gmail.com").name("name").password("password").build();
        Role role = Role.builder().id(1L).code(Role.CODE.USER).build();
        given(userRepository.getRoleByCode(Mockito.any()))
                .willReturn(role);
        // when
        userService.addRole(user, Role.CODE.USER);

        // then
        assertThat(user.getRoles().size()).isEqualTo(1);
    }
}