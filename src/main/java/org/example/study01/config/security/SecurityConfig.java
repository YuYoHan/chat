package org.example.study01.config.security;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.study01.config.jwt.JwtAccessDeniedHandler;
import org.example.study01.config.jwt.JwtAuthenticationEntryPoint;
import org.example.study01.config.jwt.JwtAuthenticationFilter;
import org.example.study01.config.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          시큐리티 기능을 사용하기 위한 클래스입니다.
 *   date : 2024/01/10
 * */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
// @EnableGlobalMethodSecurity 어노테이션은 Spring Security에서 메서드 수준의 보안 설정을 활성화하는데
// 사용되는 어노테이션입니다.
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 활성화
                .csrf(csrf -> csrf.disable())  // Disable CSRF as you're using JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        // API 권한 설정
                        .requestMatchers(HttpMethod.GET,"/", "/**").permitAll()
                        .requestMatchers("/ws/**").permitAll() // WebSocket 경로에 대한 허용
                        // 유저
                        .requestMatchers("/api/v1/users/**").permitAll() // 모든 멤버 관련 요청 허용
                        .requestMatchers("/chat-room").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/members/").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/members/{memberId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/chat/**").hasAnyRole("USER", "ADMIN")
                );

        // Enable actuator access without authentication
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/**").permitAll());

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }


    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1-based pagination
            p.setMaxPageSize(10);               // Maximum 10 items per page
        };
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:8080")); // 허용할 Origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 메서드
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 허용할 헤더
        configuration.setAllowCredentials(true); // 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정
        return source;
    }
}