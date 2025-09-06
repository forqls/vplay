package pocopoco_vplay.common.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/login**").permitAll() // 기본 공개 경로
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth2 -> oauth2   // 구글 로그인 추가
                        .loginPage("/login")        // 커스텀 로그인 페이지
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 경로
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")      // 로그아웃 후 이동
                        .permitAll()
                ).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error/**").permitAll()
                        .anyRequest().permitAll()
                );


        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint customEntryPoint() {
        return (request, response, authException) -> {
            // authException.printStackTrace(); // root cause 확인
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        };
    }
}