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
                        // 공개 경로
                        .requestMatchers("/", "/index",
                                "/css/**", "/js/**", "/img/**", "/favicon.ico",
                                "/board/**",
                                "/users/signIn", "/users/join",
                                "/users/findId", "/users/findPwd","/users/profile/**", "/users/post/**",
                                "/error/**"
                        ).permitAll()

                        // 로그인 필요한 기능
                        .requestMatchers("/board/like", "/board/download/**").authenticated()
                        .requestMatchers("/myPage/**").authenticated()

                        // 그 외는 다 인증 필요
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(login -> login
                        .loginPage("/users/signIn")  // 로그인 페이지 경로
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
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