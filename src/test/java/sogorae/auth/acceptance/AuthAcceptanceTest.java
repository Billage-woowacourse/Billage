package sogorae.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.MemberSignUpRequest;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("이메일, 비밀번호를 입력 받아 로그인 후, 인증된 토큰을 반환한다.")
    void login() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("beom@naver.com", "범고래", "12345678");
        post("/api/members", request);

        // when
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest("beom@naver.com",
          "12345678");
        ExtractableResponse<Response> response = post("/api/auth/login", loginMemberRequest);

        // then
        LoginResponse loginResponse = response.body().as(LoginResponse.class);
        assertThat(loginResponse.getAccessToken()).isNotNull();
    }
}
