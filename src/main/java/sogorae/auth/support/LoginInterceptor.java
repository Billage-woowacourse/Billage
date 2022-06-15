package sogorae.auth.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import sogorae.auth.exception.InvalidTokenException;

public class LoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public LoginInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
        if (request.getMethod().equals("POST") && request.getRequestURI().equals("/api/members")) {
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        if (!jwtProvider.isValid(token)) {
            throw new InvalidTokenException();
        }
        return true;
    }
}
