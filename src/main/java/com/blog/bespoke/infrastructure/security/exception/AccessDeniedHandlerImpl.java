package com.blog.bespoke.infrastructure.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 유저의 권한 레벨에 맞지 않는 요청을 하는 경우 호출됨
 * !!인증이 되어있는 유저에게 발생함!!
 * ex1) anonymous 만 접근 가능한 페이지에 인증 된 유저가 접근하면 실행됨
 * ex2) admin 만 접근 가능한 페이지에 어드민이 아닌 유저가 접근하면 실행됨
 */
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    /**
     * 생각해보니깐 이 경우에는 json response 가 아닌 에러 페이지를 보여줘야ㅐ하는거 아니야?
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendRedirect("/errors");
        //
        /*
         * 만약 페이지 이동이면서... htmx hx-boost 가 아니라면..
         * => 페이지 요청과 일반 api 요청은 어떻게 구분해야할까?
         *
         * 1. 페이지 요청
         *   - Hx-Boost: true & Hx-Current-Url
         *   - 새로고침
         * 2. 페이지 내에서 htmx api 요청 -> Hx-Request: true
         * 3. 일반 api 요청 -
         */

        /**
         * 1. htmx 페이지 요청
         *   - Hx-Request: true & Hx-Boost: true & Hx-Current-Url: 존재 (not empty)
         * 2. htmx api 요청
         *   - Hx-Request: true
         * 3. htmx form 요청
         *   - Hx-Request: true & Hx-Boost: true & http method is not GET
         * 3. 새로고침 페이지 요청
         *   - 아무것도 없음
         * 4. 일반 api 요청
         *   -
         */

//        request.getHeader("")

//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
//        response.getWriter().write(
//                objectMapper.writeValueAsString(
//                        EnvelopeResponse.envelope(
//                                HttpStatus.UNAUTHORIZED,
//                                "권한이 없습니다.",
//                                request.getRequestURI()
//                        ).getBody()
//                )
//        );
    }
}
