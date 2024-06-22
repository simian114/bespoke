package com.blog.bespoke.infrastructure.repository;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.repository.UserRepository;
import com.blog.bespoke.infrastructure.repository.config.RepositoryConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJdbcTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                RepositoryConfig.class,
        }
))
// 이걸 안하면 db 세팅이 embedded 로 되어버림
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserJdbcRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save test")
    void save_test() {
        // given
        User user = User.builder()
                .email("email@gmail.com")
                .nickname("nickname")
                .name("name")
                .password("password")
                .build();

        // when
        User savedUser = userRepository.save(user);
        User foundUser = userRepository.getById(savedUser.getId());

        // then
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getEmail()).isEqualTo("email@gmail.com");
        assertThat(foundUser.getCreatedAt()).isNotNull();
        assertThat(foundUser.getUpdatedAt()).isNotNull();
        assertThat(foundUser.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("email, nickname 중복 안됨")
    @Transactional
    void email_duplication_test() {
        // given
        User user = User.builder().email("email@gmail.com").nickname("nickname").name("name").password("password").build();
        User userB = User.builder().email("email@gmail.com").nickname("nicknameB").name("name").password("password").build();
        User userC = User.builder().email("emailC@gmail.com").nickname("nickname").name("name").password("password").build();

        // when
        userRepository.save(user);

        // then
        assertThrows(Exception.class, () -> userRepository.save(userB));
        assertThrows(Exception.class, () -> userRepository.save(userC));
    }
}