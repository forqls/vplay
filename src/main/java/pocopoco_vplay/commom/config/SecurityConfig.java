package pocopoco_vplay.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화용 Bean
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스는 모두 허용
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        // 홈, 회원가입, 로그인 페이지 허용
                        .requestMatchers("/", "/index", "/signup", "/login", "/oauth/**").permitAll()
                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                // 일반 로그인 (폼 기반)
                .formLogin(form -> form
                        .loginPage("/signin")         // 커스텀 로그인 페이지 (Thymeleaf 로그인 페이지 있으면 여기 연결)
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 이동할 기본 페이지
                        .permitAll()
                )
                // 소셜 로그인 (Google, Kakao)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/signin")          // 구글/카카오 로그인 버튼 있는 페이지
                        .defaultSuccessUrl("/", true) // 소셜 로그인 성공 시 이동할 기본 페이지
                )
                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")        // 로그아웃 후 홈으로 이동
                        .permitAll()
                )
                // CSRF는 필요에 따라 끄기 (API 호출 많으면 disable)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
