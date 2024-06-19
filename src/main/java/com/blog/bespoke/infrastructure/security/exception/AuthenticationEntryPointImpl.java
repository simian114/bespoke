package com.blog.bespoke.infrastructure.security.exception;

import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.ResponseEnvelope;
import com.blog.bespoke.presentation.web.exception.BasicErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        int statusCode = HttpStatus.UNAUTHORIZED.value();
        String error = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(
                new ResponseEnvelope<>(
                        BasicErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .path(request.getRequestURI())
                                .error(error)
                                .build(),
                        "username 또는 password 가 잘못되었습니다.",
                        statusCode
                ).toString()
        );
    }
}
