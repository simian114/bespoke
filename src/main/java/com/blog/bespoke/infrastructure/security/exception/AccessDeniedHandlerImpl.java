package com.blog.bespoke.infrastructure.security.exception;

import com.blog.bespoke.infrastructure.aop.ResponseEnvelope.ResponseEnvelope;
import com.blog.bespoke.presentation.web.exception.BasicErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().write(
                new ResponseEnvelope<>(
                        BasicErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .path(request.getRequestURI())
                                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                                .build(),
                        "권한 없음",
                        HttpStatus.FORBIDDEN.value()
                ).toString()
        );
    }
}
