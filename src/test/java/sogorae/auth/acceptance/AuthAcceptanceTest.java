package sogorae.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.billage.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("이메일, 비밀번호를 입력 받아 로그인 후, 인증된 토큰을 반환한다.")
    void login() {
        // given
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest("beom@naver.com", "Password12");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(loginMemberRequest)
          .when().post("/api/auth/login")
          .then().log().all()
          .extract();

        // when
        LoginResponse loginResponse = response.body().as(LoginResponse.class);

        // then
        assertThat(loginResponse.getAccessToken()).isNotNull();
    }
}
