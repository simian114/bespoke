package com.blog.bespoke.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

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
        http.exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint()) // 401
                .accessDeniedHandler(accessDeniedHandler()) // 403
        );
         */
        http.authorizeHttpRequests(requests -> requests
                // TODO: 추가해야함
                .anyRequest().permitAll()
        );

        // TODO: authentication filter 추가할것

        return http.build();
    }

}
