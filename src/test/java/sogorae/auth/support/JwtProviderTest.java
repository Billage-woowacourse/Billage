package sogorae.auth.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    private final String email = "beom@naver.com";

    @Test
    @DisplayName("이메일을 입력 받아 토큰을 생성하여 반환한다.")
    void createToken() {
        // when
        String token = jwtProvider.createToken(email);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("발급 받은 유요한 토큰을 입력 시, true를 반환한다.")
    void validateToken_true() {
        // given
        String token = jwtProvider.createToken(email);

        // when
        boolean result = jwtProvider.isValid(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 토큰 값을 입력 시, false를 반환한다.")
    void validateToken_false() {
        // given
        String token = jwtProvider.createToken(email) + "123";

        // when
        boolean result = jwtProvider.isValid(token);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("토큰에 담긴 payLoad를 조회한다.")
    void getPayload() {
        // given
        String token = jwtProvider.createToken(email);

        // when
        String getEmail = jwtProvider.getPayload(token);

        // then
        assertThat(getEmail).isEqualTo(email);
    }
}
