package sogorae.billage.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.MemberSignUpRequest;

public class BookAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("토큰과 책 제목, 책 이미지 URL, 책 상세 메세지, 책의 위치를 입력 받아 책을 등록한다.")
    void register() {
        // given
        String email = "beom@naver.com";
        String nickname = "범고래";
        String password = "12345678";
        MemberSignUpRequest request = new MemberSignUpRequest(email, nickname, password);
        post("/api/members", request);
        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        // when
        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        ExtractableResponse<Response> response = postWithToken("/api/books/",
          token, bookRegisterRequest);

        // then
        assertAll(
          () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
          () -> assertThat(response.header("Location")).isNotNull()
        );
    }

    @Test
    @DisplayName("책 id, 빌리는 멤버 email 을 받아 대여 요청을 한다.")
    void requestRent() {
        // given
        String email = "beomWhale@naver.com";
        String password = "Password";
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(email, "beom", password);
        post("/api/members", signUpRequest);

        String clientEmail = "sojukang@naver.com";
        String clientPassword = "Password";
        MemberSignUpRequest clientSignUpRequest = new MemberSignUpRequest(clientEmail, "sojukang", clientPassword);
        post("/api/members", clientSignUpRequest);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
          "책 상세 메세지", "책 위치");
        ExtractableResponse<Response> bookRegisterResponse = postWithToken("/api/books",
          token, bookRegisterRequest);

        String[] locations = bookRegisterResponse.header("Location").split("/");
        long bookId = Long.parseLong(locations[locations.length - 1]);

        LoginMemberRequest clientLoginRequest = new LoginMemberRequest(clientEmail,
          clientPassword);
        String clientToken = getTokenWithLogin(clientLoginRequest);

        // when
        ExtractableResponse<Response> response = postWithToken("/api/books/" + bookId, clientToken,
          clientEmail);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private String getTokenWithLogin(LoginMemberRequest loginMemberRequest) {
        LoginResponse loginResponse = post("/api/auth/login", loginMemberRequest).body()
          .as(LoginResponse.class);
        return loginResponse.getAccessToken();
    }
}
