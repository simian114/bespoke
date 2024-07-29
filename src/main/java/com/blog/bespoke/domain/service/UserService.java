package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserProfile;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.model.user.role.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initUser(User user, Role role, UserProfile profile) {
        user.deActivate();
        user.setUserCountInfo();
        user.setProfile(profile);
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
