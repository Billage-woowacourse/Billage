package sogorae.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sogorae.auth.dto.LoginGithubRequest;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.auth.service.AuthService;
import sogorae.billage.domain.Member;
import sogorae.billage.service.LoginGithubService;
import sogorae.billage.service.MemberService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final LoginGithubService loginGithubService;

    public AuthController(AuthService authService, MemberService memberService,
        LoginGithubService loginGithubService) {
        this.authService = authService;
        this.memberService = memberService;
        this.loginGithubService = loginGithubService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginMemberRequest request) {
        Member member = memberService.findByEmailAndPassword(request.getEmail(),
            request.getPassword());
        return authService.createToken(member);
    }

    @PostMapping("/login/github")
    public LoginResponse loginGithub(@RequestBody LoginGithubRequest request) {
        return authService.createToken(loginGithubService.login(request.getCode()));
    }
}
