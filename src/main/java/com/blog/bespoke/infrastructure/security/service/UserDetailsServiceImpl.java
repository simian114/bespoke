package com.blog.bespoke.infrastructure.security.service;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.UserRelation;
import com.blog.bespoke.domain.repository.user.UserRepository;
import com.blog.bespoke.infrastructure.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email, UserRelation.builder().roles(true).avatar(true).build())
                .orElseThrow(() -> new UsernameNotFoundException(email + " 없음"));
        return new UserPrincipal(user);
    }
}
