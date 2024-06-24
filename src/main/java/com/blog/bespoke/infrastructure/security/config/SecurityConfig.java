package com.blog.bespoke.infrastructure.security.config;

import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.service.JwtService;
import com.blog.bespoke.infrastructure.security.exception.AccessDeniedHandlerImpl;
import com.blog.bespoke.infrastructure.security.exception.AuthenticationEntryPointImpl;
import com.blog.bespoke.infrastructure.security.filter.JwtAuthenticationFilter;
import com.blog.bespoke.infrastructure.web.filter.transaction.TransactionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl(objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl(objectMapper);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 기본 로그인 방법 무효화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 세션 사용 안함
        http.sessionManagement(configurer ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        /*
        TODO: 401, 403 exception handling
         */
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint()) // 401
                .accessDeniedHandler(accessDeniedHandler()) // 403
        );

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/api/admin/**").hasRole(Role.CODE.ADMIN.name())
                .anyRequest().permitAll()
        );

        http.addFilterAt(new TransactionFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(jwtAuthenticationFilter(), TransactionFilter.class);

        return http.build();
    }

}
