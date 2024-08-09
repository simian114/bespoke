package com.blog.bespoke.infrastructure.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 유저의 인증 여부에 따라 실행됨.
 * 예를 들면, 인증 된 유저가 접근 가능한 페이지에 인증안된 유저가 접근하면
 * <p>
 * 만약, 인증 여부가 아니라 인증 된 유저인데 유저의 권한이 맞지 않는 경우에 대한 핸들링은 AccessDeniedHandler 에서 담당
 */
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/errors");
    }
}
