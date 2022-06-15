package sogorae.billage.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.MemberSignUpRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("이메일, 닉네임, 비밀번호로 회원가입을 한다.")
    void create() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("beom@naver.com", "범고래", "12345678");

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/api/members")
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.header("Location")).isNotNull()
        );
    }
}
