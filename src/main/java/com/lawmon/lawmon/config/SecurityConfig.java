package com.lawmon.lawmon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (필요한 경우)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll() // 비인증 사용자 접근 허용
                        .anyRequest().permitAll() // 나머지 요청은 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/auth/login") // 로그인 페이지 설정 (필요하면 변경)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}