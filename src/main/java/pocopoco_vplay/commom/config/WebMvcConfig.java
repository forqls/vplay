package pocopoco_vplay.commom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pocopoco_vplay.commom.intercepter.CheckLoginIntercepter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginIntercepter())
                .addPathPatterns("/users/**", "/myPage/**", "")
                .excludePathPatterns("/users/signIn", "/users/signup", "/myPage/creator_page", "/myPage/Pricing");
    }


}
