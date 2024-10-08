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

        // NOTE: jwt 를 사용하기 때문에 안해도된다..?
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

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
                        // admin
                        .requestMatchers("/admin/**").hasRole(Role.Code.ADMIN.name())
                        // error
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/errors").permitAll()
                        // 실제 서비스
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/signup").anonymous()
                        .requestMatchers("/signup/success").anonymous()
                        .requestMatchers("/email-validation").anonymous()
                        .requestMatchers("/pwa/**").permitAll()
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
                        .requestMatchers("/banner/**").permitAll()
                        .requestMatchers("/blog/posts/{postId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/blog/posts/{postId}/comments").permitAll()
//                .requestMatchers("/blog/manage/banners/**").hasRole(Role.Code.ADVERTISER.name())
                        // payment
                        .requestMatchers(HttpMethod.GET, "/payment/**").authenticated()
                        .requestMatchers("/blog/manage/**").authenticated()
                        .requestMatchers("/blog/{nickname}/{postId}").permitAll()
                        .requestMatchers("/blog/{nickname}").permitAll()
                        .requestMatchers("/blog/{nickname}/posts/**").permitAll()
                        .requestMatchers("/blog/{nickname}/category/**").permitAll()
                        .requestMatchers("/signup/success").anonymous()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterAt(new TransactionFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(jwtAuthenticationFilter(), TransactionFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilter(), CsrfFilter.class);

        return http.build();
    }

}
