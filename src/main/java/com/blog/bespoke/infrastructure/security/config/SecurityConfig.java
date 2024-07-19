package com.blog.bespoke.infrastructure.security.config;

import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.service.JwtService;
import com.blog.bespoke.infrastructure.security.exception.AccessDeniedHandlerImpl;
import com.blog.bespoke.infrastructure.security.exception.AuthenticationEntryPointImpl;
import com.blog.bespoke.infrastructure.security.filter.JwtAuthenticationFilter;
import com.blog.bespoke.infrastructure.web.filter.transaction.TransactionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Set;

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
        // TODO: 서버로 전변경하면 csrf 옵션 키기. 현재는 일반 API 때문에 disable
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

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
                // 과거 개발용 api
                .requestMatchers("/api/admin/**").hasRole(Role.Code.ADMIN.name())
                .requestMatchers("/api/user").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/reissue").permitAll()
                .requestMatchers("/api/auth/test").authenticated()
                .requestMatchers("/api/user/{id}/follow").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                // 실제 서비스
                .requestMatchers("/login").permitAll()
                .requestMatchers("/signup").permitAll()
                .requestMatchers(
                        PathRequest.toStaticResources().at(
                                Set.of(
                                        StaticResourceLocation.CSS,
                                        StaticResourceLocation.JAVA_SCRIPT
                                )
                        )).permitAll()
                .requestMatchers("/webfonts/**").permitAll()
                .requestMatchers("/noti/{nickname}").permitAll()
                .requestMatchers("/hx/home/posts").permitAll()
                .requestMatchers("/blog/{nickname}/{postId}").permitAll()
                .requestMatchers("/blog/{nickname}").permitAll()
                .requestMatchers("/blog/{nickname}/posts/**").permitAll()
                .requestMatchers("/blog/{nickname}/category/**").permitAll()
                .requestMatchers("/blog/manage").authenticated()
                .requestMatchers("/signup/success").anonymous()
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterAt(new TransactionFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(jwtAuthenticationFilter(), TransactionFilter.class);

        return http.build();
    }

}
