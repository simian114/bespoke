package com.blog.bespoke.infrastructure.repository;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserSearchCond;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.model.user.role.UserRole;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.infrastructure.repository.config.RepositoryConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                RepositoryConfig.class,
        }
))
// 이걸 안하면 db 세팅이 embedded 로 되어버림
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class UserRepositoryImplTest {
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

    @Test
    @DisplayName("리스트 테스트")
    @Transactional
    void search_test() {
        // given
        Role adminRole = userRepository.getRoleByCode(Role.Code.ADMIN);
        Role userRole = userRepository.getRoleByCode(Role.Code.USER);

        User userA = User.builder().email("emailA@gmail.com").nickname("nicknameA").name("name").password("password").build();
        User userB = User.builder().email("emailB@gmail.com").nickname("nicknameB").name("name").password("password").build();

        UserRole userARole = UserRole.builder().user(userA).role(adminRole).build();
        UserRole userBRole = UserRole.builder().user(userB).role(userRole).build();

        userA.addRole(userARole);
        userB.addRole(userBRole);

        // 배열로 할 수 있게 해야함
        UserSearchCond cond = UserSearchCond.builder().build();

        // when
        userRepository.save(userA);
        userRepository.save(userB);

        // then
        Page<User> search = userRepository.search(cond, PageRequest.of(0, 10));

        assertThat(search.getTotalElements()).isEqualTo(2);
        assertThat(search.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("role 을 이용해 필터링 할 수 있음")
    void search_role_test() {
        // given
        Role adminRole = userRepository.getRoleByCode(Role.Code.ADMIN);
        Role userRole = userRepository.getRoleByCode(Role.Code.USER);

        User userA = User.builder().email("emailA@gmail.com").nickname("nicknameA").name("name").password("password").build();
        User userB = User.builder().email("emailB@gmail.com").nickname("nicknameB").name("name").password("password").build();

        UserRole userARole = UserRole.builder().user(userA).role(adminRole).build();
        UserRole userBRole = UserRole.builder().user(userB).role(userRole).build();

        userA.addRole(userARole);
        userB.addRole(userBRole);

        // 배열로 할 수 있게 해야함
        UserSearchCond cond = UserSearchCond.builder().role(Role.Code.ADMIN).build();
        UserSearchCond cond2 = UserSearchCond.builder().role(Role.Code.USER).build();

        // when
        userRepository.save(userA);
        userRepository.save(userB);

        // then
        Page<User> adminUserSearch = userRepository.search(cond, PageRequest.of(0, 10));
        Page<User> userSearch = userRepository.search(cond2, PageRequest.of(0, 10));

        assertThat(adminUserSearch.getTotalElements()).isEqualTo(1);
        assertThat(adminUserSearch.getContent().size()).isEqualTo(1);
        assertThat(userSearch.getTotalElements()).isEqualTo(1);
        assertThat(userSearch.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("status 을 이용해 필터링 할 수 있음")
    void search_status_test() {
        // given
        Role adminRole = userRepository.getRoleByCode(Role.Code.ADMIN);
        Role userRole = userRepository.getRoleByCode(Role.Code.USER);

        User userA = User.builder().email("emailA@gmail.com").nickname("nicknameA").name("name").password("password").status(User.Status.ACTIVE).build();
        User userB = User.builder().email("emailB@gmail.com").nickname("nicknameB").name("name").password("password").status(User.Status.INACTIVE).build();

        UserRole userARole = UserRole.builder().user(userA).role(adminRole).build();
        UserRole userBRole = UserRole.builder().user(userB).role(userRole).build();

        userA.addRole(userARole);
        userB.addRole(userBRole);

        // 배열로 할 수 있게 해야함
        UserSearchCond cond = UserSearchCond.builder().status(User.Status.INACTIVE).build();
        UserSearchCond cond2 = UserSearchCond.builder().status(User.Status.ACTIVE).build();

        // when
        userRepository.save(userA);
        userRepository.save(userB);

        // then
        Page<User> adminUserSearch = userRepository.search(cond, PageRequest.of(0, 10));
        Page<User> userSearch = userRepository.search(cond2, PageRequest.of(0, 10));
        Page<User> noCondSearch = userRepository.search(null, PageRequest.of(0, 10));

        assertThat(adminUserSearch.getTotalElements()).isEqualTo(1);
        assertThat(adminUserSearch.getContent().size()).isEqualTo(1);
        assertThat(userSearch.getTotalElements()).isEqualTo(1);
        assertThat(userSearch.getContent().size()).isEqualTo(1);
        assertThat(noCondSearch.getTotalElements()).isEqualTo(2);
        assertThat(noCondSearch.getContent().size()).isEqualTo(2);

    }

}