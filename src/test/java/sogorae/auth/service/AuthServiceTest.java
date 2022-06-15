package sogorae.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogorae.auth.dto.LoginResponse;
import sogorae.auth.support.JwtProvider;
import sogorae.billage.domain.Member;
import sogorae.billage.dto.MemberSignUpRequest;
import sogorae.billage.service.MemberService;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("이메일과 비밀번호를 입력 받아, 유효한 회원이면 토큰을 발급하여 반환한다.")
    void login() {
        // given
        String email = "beom@naver.com";
        String password = "Password12";
        String nickname = "beom";
        MemberSignUpRequest request = new MemberSignUpRequest(email, nickname, password);
        memberService.save(request);
        Member member = new Member(email, nickname, password);

        // when
        LoginResponse loginResponse = authService.createToken(member);

        // then
        boolean isValid = jwtProvider.isValid(loginResponse.getAccessToken());
        String resultEmail = jwtProvider.getPayload(loginResponse.getAccessToken());
        assertAll(
          () -> assertThat(isValid).isTrue(),
          () -> assertThat(resultEmail).isEqualTo(email)
        );
    }
}
