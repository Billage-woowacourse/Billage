package sogorae.billage.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sogorae.billage.AcceptanceTest;
import sogorae.billage.dto.MemberSignUpRequest;

public class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("이메일, 닉네임, 비밀번호로 회원가입을 한다.")
    void create() {
        // given
        MemberSignUpRequest request = new MemberSignUpRequest("beom@naver.com", "범고래", "12345678");

        // when
        ExtractableResponse<Response> response = post("/api/members", request);

        // then
        assertAll(
          () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
          () -> assertThat(response.header("Location")).isNotNull()
        );
    }
}
