package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.request.LoginRequestDto;
import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.service.JwtService;
import com.blog.bespoke.infrastructure.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String login(LoginRequestDto requestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword(),
                        null
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authenticate.getPrincipal();
        User user = userPrincipal.getUser();
        return jwtService.createAccessToken(user);
    }
}
