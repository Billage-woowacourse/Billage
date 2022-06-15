package sogorae.auth.service;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import sogorae.auth.dto.LoginMember;
import sogorae.auth.dto.LoginResponse;
import sogorae.auth.exception.InvalidTokenException;
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

    public LoginResponse createToken(Member member) {
        try {
            String token = jwtProvider.createToken(member.getEmail());
            return new LoginResponse(token);
        } catch (RuntimeException e) {
            throw new InvalidTokenException();
        }
    }

    public LoginMember findMemberByToken(String token) {
        String email = getPayload(token);
        Member member = memberService.findByEmail(email);
        return LoginMember.from(member);
    }

    private String getPayload(String token) {
        try {
            return jwtProvider.getPayload(token);
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }
}
