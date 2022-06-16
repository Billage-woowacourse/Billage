package sogorae.billage;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AcceptanceTest {

    @Value("${local.server.port}")
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public ExtractableResponse<Response> get(String url) {
        return RestAssured.given().log().all()
          .when().get(url)
          .then().log().all()
          .extract();
    }

    public ExtractableResponse<Response> getWithToken(String url, String token) {
        return RestAssured.given().log().all()
          .header("Authorization", "Bearer " + token)
          .when().get(url)
          .then().log().all()
          .extract();
    }

    public ExtractableResponse<Response> post(String url, Object request) {
        return RestAssured.given().log().all()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(request)
          .when().post(url)
          .then().log().all()
          .extract();
    }

    public ExtractableResponse<Response> postWithToken(String url, String token, Object request) {
        return RestAssured.given().log().all()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .header("Authorization", "Bearer " + token)
          .body(request)
          .when().post(url)
          .then().log().all()
          .extract();
    }

    public ExtractableResponse<Response> putWithToken(String url, String token, Object request) {
        return RestAssured.given().log().all()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .header("Authorization", "Bearer " + token)
          .body(request)
          .when().put(url)
          .then().log().all()
          .extract();
    }
}
