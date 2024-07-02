package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.model.user.role.UserRole;
import com.blog.bespoke.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void addRole(User user, Role.Code code) {
        Role role = userRepository.getRoleByCode(code);
        UserRole userRole = UserRole.builder()
                .role(role)
                .user(user)
                .build();
        user.addRole(userRole);
    }

    public User getById(Long id) {
        return userRepository.getById(id);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
