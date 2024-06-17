package com.blog.bespoke.infrastructure.repository;

import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.infrastructure.entity.jpa.mapper.UserEntityMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                UserRepositoryImpl.class, UserEntityMapper.class
        })
)
@Transactional
class UserRepositoryImplTest {
    @Autowired
    private UserRepositoryImpl userRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("save test")
    void save_test() {
        // give
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
        // give
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