package sogorae.billage.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import sogorae.auth.dto.LoginMemberRequest;
import sogorae.auth.dto.LoginResponse;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.domain.LentStatus;
import sogorae.billage.domain.Status;
import sogorae.billage.dto.AllowLentRequest;
import sogorae.billage.dto.BookClientResponse;
import sogorae.billage.dto.BookLentRequest;
import sogorae.billage.dto.BookRegisterRequest;
import sogorae.billage.dto.BookResponse;
import sogorae.billage.dto.BookUpdateRequest;
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
    void requestLent() {
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

        long bookId = getBookId(bookRegisterResponse);

        LoginMemberRequest clientLoginRequest = new LoginMemberRequest(clientEmail,
            clientPassword);
        String clientToken = getTokenWithLogin(clientLoginRequest);

        // when
        BookLentRequest bookLentRequest = new BookLentRequest("빌림 요청");
        ExtractableResponse<Response> response = postWithToken("/api/books/" + bookId, clientToken,
          bookLentRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("책 id, 빌리는 멤버 email 을 받아 대여 요청을 수락한다.")
    void allowLent() {
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

        long bookId = getBookId(bookRegisterResponse);

        LoginMemberRequest clientLoginRequest = new LoginMemberRequest(clientEmail,
            clientPassword);
        String clientToken = getTokenWithLogin(clientLoginRequest);
        BookLentRequest bookLentRequest = new BookLentRequest("빌림 요청");
        postWithToken("/api/books/" + bookId, clientToken, bookLentRequest);

        // when
        AllowLentRequest allowLentRequest = new AllowLentRequest("allow");

        ExtractableResponse<Response> response = postWithToken(
            "/api/books/" + bookId + "/lents", token, allowLentRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("client가 빌림 요청 or 빌리고 있는 책들을 조회한다.")
    void findAllByClient() {
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

        BookRegisterRequest bookRegisterRequest1 = new BookRegisterRequest("책 제목1", "image_url1",
          "책 상세 메세지1", "책 위치2");
        ExtractableResponse<Response> bookRegisterResponse1 = postWithToken("/api/books",
          token, bookRegisterRequest1);
        BookRegisterRequest bookRegisterRequest2 = new BookRegisterRequest("책 제목1", "image_url2",
          "책 상세 메세지2", "책 위치2");
        ExtractableResponse<Response> bookRegisterResponse2 = postWithToken("/api/books",
          token, bookRegisterRequest2);

        long bookId1 = getBookId(bookRegisterResponse1);
        long bookId2 = getBookId(bookRegisterResponse2);

        LoginMemberRequest clientLoginRequest = new LoginMemberRequest(clientEmail,
          clientPassword);
        String clientToken = getTokenWithLogin(clientLoginRequest);
        BookLentRequest bookLentRequest = new BookLentRequest("빌림 요청");
        postWithToken("/api/books/" + bookId1, clientToken, bookLentRequest);
        postWithToken("/api/books/" + bookId2, clientToken, bookLentRequest);

        // when
        AllowLentRequest allowLentRequest = new AllowLentRequest("allow");
        postWithToken("/api/books/" + bookId1 + "/lents", token, allowLentRequest);

        ExtractableResponse<Response> response = getWithToken("/api/books/client", clientToken);
        List<BookClientResponse> actual = response.body().jsonPath().getList(".", BookClientResponse.class);
        BookClientResponse lentBook = actual.stream()
          .filter(i -> (i.getStatus().equals(LentStatus.LENT.name())))
          .findAny().get();
        BookClientResponse requestBook = actual.stream()
          .filter(i -> (i.getStatus().equals(LentStatus.REQUEST.name())))
          .findAny().get();

        assertAll(
          () -> assertThat(actual.size()).isEqualTo(2),
          () -> assertThat(lentBook.getTitle()).isEqualTo(bookRegisterRequest1.getTitle()),
          () -> assertThat(requestBook.getTitle()).isEqualTo(bookRegisterRequest2.getTitle())
        );
    }

    @Test
    @DisplayName("책 id, 빌리는 멤버 email 을 받아 대여 요청을 거절한다.")
    void allowLent_deny() {
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
        BookLentRequest bookLentRequest = new BookLentRequest("빌림 요청");
        postWithToken("/api/books/" + bookId, clientToken, bookLentRequest);

        // when
        AllowLentRequest allowLentRequest = new AllowLentRequest("deny");
        ExtractableResponse<Response> response = postWithToken(
            "/api/books/" + bookId + "/lents", token, allowLentRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("책 id, 책 owner 멤버 email 을 받아 반납을 완료한다.")
    void returning() {
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

        long bookId = getBookId(bookRegisterResponse);

        LoginMemberRequest clientLoginRequest = new LoginMemberRequest(clientEmail,
            clientPassword);
        String clientToken = getTokenWithLogin(clientLoginRequest);

        // when
        BookLentRequest bookLentRequest = new BookLentRequest("빌림 요청");
        postWithToken("/api/books/" + bookId, clientToken, bookLentRequest);

        AllowLentRequest allowLentRequest = new AllowLentRequest("allow");
        postWithToken("/api/books/" + bookId + "/lents", token, allowLentRequest);

        ExtractableResponse<Response> response = putWithToken("/api/books/" + bookId, token, clientEmail);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("책 id 를 받아 단일 책 정보를 조회한다.")
    void findOne() {
        // given
        String email = "beom@naver.com";
        String nickname = "범고래";
        String password = "12345678";
        MemberSignUpRequest request = new MemberSignUpRequest(email, nickname, password);
        post("/api/members", request);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
            "책 상세 메세지", "책 위치");
        ExtractableResponse<Response> bookRegisterResponse = postWithToken("/api/books/", token, bookRegisterRequest);

        long bookId = getBookId(bookRegisterResponse);

        // when
        ExtractableResponse<Response> response = get("/api/books/" + bookId);

        // then
        BookResponse expected = new BookResponse(bookId, nickname, "책 제목", "image_url",
            "책 상세 메세지", "책 위치", Status.AVAILABLE.name());
        BookResponse actual = response.body().as(BookResponse.class);

        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    private long getBookId(ExtractableResponse<Response> bookRegisterResponse) {
        String[] locations = bookRegisterResponse.header("Location").split("/");
        return Long.parseLong(locations[locations.length - 1]);
    }

    @Test
    @DisplayName("전체 책 정보를 조회한다.")
    void findAll() {
        // given
        String email = "beom@naver.com";
        String nickname = "범고래";
        String password = "12345678";
        MemberSignUpRequest request = new MemberSignUpRequest(email, nickname, password);
        post("/api/members", request);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
            "책 상세 메세지", "책 위치");
        Long bookId1 = getBookId(postWithToken("/api/books/", token, bookRegisterRequest));

        BookRegisterRequest bookRegisterRequest2 = new BookRegisterRequest("책 제목2", "image_url2",
            "책 상세 메세지2", "책 위치2");
        Long bookId2 = getBookId(postWithToken("/api/books/", token, bookRegisterRequest2));

        // when
        ExtractableResponse<Response> response = get("/api/books");

        // then
        List<BookResponse> expected = List.of(
            new BookResponse(bookId1, nickname, "책 제목", "image_url",
                "책 상세 메세지", "책 위치", Status.AVAILABLE.name()),
            new BookResponse(bookId2, nickname, "책 제목2", "image_url2",
                "책 상세 메세지2", "책 위치2", Status.AVAILABLE.name()));

        List<BookResponse> actual = response.body().jsonPath().getList(".", BookResponse.class);

        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("사용자 email과 bookId를 입력 받아, 책을 제거한다.")
    void delete() {
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

        long bookId = getBookId(bookRegisterResponse);

        LoginMemberRequest clientLoginRequest = new LoginMemberRequest(clientEmail,
            clientPassword);
        getTokenWithLogin(clientLoginRequest);

        // when
        ExtractableResponse<Response> response = deleteWithToken("/api/books/" + bookId, token, email);

        // then
        ExtractableResponse<Response> responseAllBooks = get("/api/books");
        List<BookResponse> actual = responseAllBooks.body().jsonPath().getList(".", BookResponse.class);

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(actual).hasSize(0)
        );
    }

    @Test
    @DisplayName("BookUpdateRequest, bookId, token을 받아 책의 정보를 수정한다.")
    void update() {
        // given
        String email = "beomWhale@naver.com";
        String password = "Password";
        MemberSignUpRequest signUpRequest = new MemberSignUpRequest(email, "beom", password);
        post("/api/members", signUpRequest);

        LoginMemberRequest loginMemberRequest = new LoginMemberRequest(email, password);
        String token = getTokenWithLogin(loginMemberRequest);

        BookRegisterRequest bookRegisterRequest = new BookRegisterRequest("책 제목", "image_url",
            "책 상세 메세지", "책 위치");
        ExtractableResponse<Response> bookRegisterResponse = postWithToken("/api/books",
            token, bookRegisterRequest);

        String[] locations = bookRegisterResponse.header("Location").split("/");
        long bookId = Long.parseLong(locations[locations.length - 1]);

        // when
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest("수정장소", "수정 상세 메세지");
        ExtractableResponse<Response> response = putWithToken(
            "/api/books/me/" + bookId, token, bookUpdateRequest);

        ExtractableResponse<Response> updatedBookResponse = get("api/books/" + bookId);
        BookResponse bookResponse = updatedBookResponse.as(BookResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookResponse.getLocation()).isEqualTo("수정장소");
        assertThat(bookResponse.getDetailMessage()).isEqualTo("수정 상세 메세지");
    }

    private String getTokenWithLogin(LoginMemberRequest loginMemberRequest) {
        LoginResponse loginResponse = post("/api/auth/login", loginMemberRequest).body()
            .as(LoginResponse.class);
        return loginResponse.getAccessToken();
    }
}
