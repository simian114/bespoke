package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.model.user.role.UserRole;
import com.blog.bespoke.domain.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initUser(User user, Role role) {
        user.init();
        user.changePassword(encodePassword(user.getPassword()));
        // create
        UserRole userRole = UserRole.builder()
                .role(role)
                .user(user)
                .build();
        user.addRole(userRole);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
