package tave.crezipsa.crezipsa.global.config;
// 인증 인가 및 설정 하는곳

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tave.crezipsa.crezipsa.global.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // csrf 공격 빙지/세션 기반 로그인 필요 O, jwt는 X
                .csrf(csrf -> csrf.disable())

                // 세션 X
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증/인가 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()// 로그인 관련 API는 인증 없이 접근 가능
                        .requestMatchers("/api/comment/**").permitAll()
                        .requestMatchers("/api/user/signUp").permitAll()
                        .anyRequest().authenticated()             // 그 외는 인증 필요
                )

                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter(스프링 기본 로그인 필터) 앞에 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
