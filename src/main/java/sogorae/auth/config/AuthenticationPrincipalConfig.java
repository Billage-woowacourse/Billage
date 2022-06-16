package sogorae.auth.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sogorae.auth.controller.AuthenticationArgumentResolver;
import sogorae.auth.service.AuthService;
import sogorae.auth.support.JwtProvider;
import sogorae.auth.support.LoginInterceptor;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    public AuthenticationPrincipalConfig(JwtProvider jwtProvider, AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtProvider))
          .addPathPatterns("/api/**")
          .excludePathPatterns("/api/members", "/api/auth/login", "/api/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(createAuthenticationArgumentResolver());
    }

    @Bean
    public AuthenticationArgumentResolver createAuthenticationArgumentResolver() {
        return new AuthenticationArgumentResolver(authService);
    }
}
