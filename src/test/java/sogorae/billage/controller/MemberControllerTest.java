package sogorae.billage.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.MemberSignUpRequest;

public class MemberControllerTest extends AcceptanceTest {

    @ParameterizedTest
    @DisplayName("잘못된 이메일로 회원가입 요청할 경우 400 응답을 반환한다.")
    @ValueSource(strings = {"sojukang", "@naver.com", "sojukang@@naver.com"})
    void saveBadRequestEmail(String email) {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest(email, "소주캉", "12345678");

        // when
        ExtractableResponse<Response> response = post("/api/members", request);

        // then
        assertAll(
          () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
          () -> assertThat(response.body().jsonPath().getString("message")).isEqualTo(
            "이메일 형식에 맞지 않습니다.")
        );
    }

    @ParameterizedTest
    @DisplayName("잘못된 닉네임으로 회원가입 요청할 경우 400 응답을 반환한다.")
    @CsvSource(value = {"a:0", "a:40"}, delimiter = ':')
    void saveBadRequestEmail(String nicknameChar, int count) {
        // given
        String nickname = nicknameChar.repeat(count);
        MemberSignUpRequest request = new MemberSignUpRequest("sojukang@naver.com", nickname,
          "12345678");

        // when
        ExtractableResponse<Response> response = post("/api/members", request);

        // then
        assertAll(
          () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
          () -> assertThat(response.body().jsonPath().getString("message")).isEqualTo(
            "닉네임 형식에 맞지 않습니다.")
        );
    }

    @ParameterizedTest
    @DisplayName("잘못된 비밀번호로 회원가입 요청할 경우 400 응답을 반환한다.")
    @CsvSource(value = {"a:7", "a:21"}, delimiter = ':')
    void saveBadRequestPassword(String passwordChar, int count) {
        // given
        String password = passwordChar.repeat(count);
        MemberSignUpRequest request = new MemberSignUpRequest("sojukang@naver.com", "소주캉",
          password);

        // when
        ExtractableResponse<Response> response = post("/api/members", request);

        // then
        assertAll(
          () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
          () -> assertThat(response.body().jsonPath().getString("message")).isEqualTo(
            "비밀번호 형식에 맞지 않습니다.")
        );
    }
}
