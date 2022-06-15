package sogorae.auth.service;

import org.springframework.stereotype.Service;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.auth.exception.LoginFailException;
import sogorae.auth.support.JwtProvider;
import sogorae.billage.domain.Member;
import sogorae.billage.service.MemberService;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AuthService(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    public LoginResponse createToken(LoginMemberRequest loginMemberRequest) {
        try {
            Member member = memberService.findByEmailAndPassword(loginMemberRequest.getEmail(),
              loginMemberRequest.getPassword());
            String token = jwtProvider.createToken(member.getEmail());
            return new LoginResponse(token);
        } catch (RuntimeException e) {
            throw new LoginFailException(e.getCause());
        }
    }
}
