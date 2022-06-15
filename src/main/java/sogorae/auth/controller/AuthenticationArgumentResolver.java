package sogorae.auth.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sogorae.auth.dto.LoginMember;
import sogorae.auth.service.AuthService;
import sogorae.auth.support.AuthenticationPrincipal;
import sogorae.auth.support.AuthorizationExtractor;

public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public AuthenticationArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = AuthorizationExtractor.extract(request);
        return authService.findMemberByToken(token);
    }
}
