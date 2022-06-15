package sogorae.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.auth.exception.LoginFailException;
import sogorae.auth.support.JwtProvider;
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
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", password);
        memberService.save(request);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);

        // when
        LoginResponse loginResponse = authService.createToken(loginMemberRequest);

        // then
        boolean isValid = jwtProvider.isValid(loginResponse.getAccessToken());
        String resultEmail = jwtProvider.getPayload(loginResponse.getAccessToken());
        assertAll(
          () -> assertThat(isValid).isTrue(),
          () -> assertThat(resultEmail).isEqualTo(email)
        );
    }

    @Test
    @DisplayName("가입되지 않은 회원 정보로 로그인 시, 예외가 발생한다.")
    void loginExceptionInValidMember() {
        // given
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest("beom@naver.com",
          "Password12");

        // when, then
        assertThatThrownBy(() ->
          authService.createToken(loginMemberRequest)).isInstanceOf(LoginFailException.class)
          .hasMessage("로그인에 실패했습니다.");
    }
}
