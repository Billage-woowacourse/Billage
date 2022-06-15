package sogorae.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sogorae.auth.support.JwtProvider;
import sogorae.auth.support.LoginInterceptor;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;


    public AuthenticationPrincipalConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtProvider))
          .addPathPatterns("/api/**")
          .excludePathPatterns("/api/members", "/api/auth/login");
    }
}
