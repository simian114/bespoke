package com.blog.bespoke.infrastructure.security.filter;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.service.JwtService;
import com.blog.bespoke.infrastructure.security.principal.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> accessToken = getAccessToken(request);
        if (accessToken.isEmpty() || !isValidAccessTokenAndSetRequestContext(accessToken.get(), request)) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = jwtService.parseAccessTokenToUser(accessToken.get());
        UserPrincipal userPrincipal = new UserPrincipal(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }

    private Optional<String> getAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(JwtService.AUTH_HEADER);
        if (authorization == null || !authorization.startsWith(JwtService.AUTH_SCHEME)) {
            return Optional.empty();
        }
        String accessToken = authorization.substring(JwtService.AUTH_SCHEME.length() + 1);
        if (accessToken.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(accessToken);
    }


    /**
     * 각 예외 상황마다 request 에 값 세팅할지 고민해보기
     */
    private boolean isValidAccessTokenAndSetRequestContext(String accessToken, HttpServletRequest request) {
        try {
            jwtService.checkAccessTokenValidity(accessToken);
        } catch (ExpiredJwtException e) {
            request.setAttribute("expired", true);
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (SignatureException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}

