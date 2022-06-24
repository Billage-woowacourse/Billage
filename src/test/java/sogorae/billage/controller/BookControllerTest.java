package sogorae.billage.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.MemberSignUpRequest;

public class BookControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("책을 등록한 멤버가 본인의 책을 대여 요청할 시 400 응답을 반환한다.")
    void requestRentExceptionInvalidMember() {
        // given
        String email = "beomWhale@naver.com";
        String password = "Password";
        MemberSignUpRequest request = new MemberSignUpRequest(email, "beom", password);
        post("/api/members", request);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
            "책 상세 메세지", "책 위치");
        ExtractableResponse<Response> bookRegisterResponse = postWithToken("/api/books",
            token, bookRegisterRequest);

        String[] locations = bookRegisterResponse.header("Location").split("/");
        long bookId = Long.parseLong(locations[locations.length - 1]);

        // when
        ExtractableResponse<Response> response = postWithToken("/api/books/" + bookId, token, email);

        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
            () -> assertThat(response.body().jsonPath().getString("message")).isEqualTo("주인은 대여 요청할 수 없습니다.")
        );
    }

    @Test
    @DisplayName("없는 BookId를 입력 받아 조회할 경우 404 응답을 반환한다.")
    void findOne() {
        // given
        String email = "beom@naver.com";
        String nickname = "범고래";
        String password = "12345678";
        MemberSignUpRequest request = new MemberSignUpRequest(email, nickname, password);
        post("/api/members", request);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        // when
        ExtractableResponse<Response> response = getWithToken("api/books/" + 99L, token);

        // then
        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
            () -> assertThat(response.body().jsonPath().getString("message")).isEqualTo("해당 책이 존재하지 않습니다.")
        );

    }

    private String getTokenWithLogin(LoginMemberRequest loginMemberRequest) {
        LoginResponse loginResponse = post("/api/auth/login", loginMemberRequest).body()
            .as(LoginResponse.class);
        return loginResponse.getAccessToken();
    }
}
