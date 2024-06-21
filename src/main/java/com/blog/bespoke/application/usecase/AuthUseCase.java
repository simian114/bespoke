package com.blog.bespoke.application.usecase;

import com.blog.bespoke.application.dto.request.LoginRequestDto;
import com.blog.bespoke.application.exception.BusinessException;
import com.blog.bespoke.application.exception.ErrorCode;
import com.blog.bespoke.domain.model.User;
import com.blog.bespoke.domain.service.JwtService;
import com.blog.bespoke.infrastructure.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String login(LoginRequestDto requestDto) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (LockedException e) {
            throw new BusinessException(ErrorCode.USER_HAS_BLOCKED);
        } catch (DisabledException e) {
            throw new BusinessException(ErrorCode.USER_INACTIVE);
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCode.USER_FAIL_AUTHENTICATION);
        }
        UserPrincipal userPrincipal = (UserPrincipal) authenticate.getPrincipal();
        User user = userPrincipal.getUser();
        return jwtService.createAccessToken(user);
    }
}
