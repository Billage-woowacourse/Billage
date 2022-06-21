package sogorae.billage.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.MemberSignUpRequest;

class NaverSerchControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("로그인한 사용자가 키워드를 입력하여 SearchBookResponse를 반환받는다.")
    void search() {
        String email = "beomWhale@naver.com";
        String password = "Password";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", password);
        post("/api/members", request);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        String keyword = "객체지향";
        ExtractableResponse<Response> response = getWithToken("/api/naver/" + keyword, token);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
    }

    private String getTokenWithLogin(LoginMemberRequest loginMemberRequest) {
        LoginResponse loginResponse = post("/api/auth/login", loginMemberRequest).body()
          .as(LoginResponse.class);
        return loginResponse.getAccessToken();
    }
}
