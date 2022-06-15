package sogorae.billage.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import sogorae.billage.domain.Member;

public class MemberTest {

    @ParameterizedTest
    @CsvSource(value = {"a:46", "a:1"}, delimiter = ':')
    @DisplayName("정상 이메일 입력 받아, 회원가입을 한다.")
    void signUpCorrectEmail(String emailChar, int count) {
        // given
        String email = emailChar.repeat(count) + "@a.a";

        // when
        Member member = new Member(email, "beom", "password123");

        // then
        assertThat(member).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("이메일 형식에 맞지 않을 시, 예외가 발생한다.")
    @ValueSource(strings = {"@naver.com", "boem", "boem@@naver.com"})
    @NullSource
    void signUpExceptionInValidEmailFormat(String email) {
        // when, then
        Assertions.assertThatThrownBy(() -> new Member(email, "범고래", "12345678"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이메일 형식에 맞지 않습니다.");
    }

    @Test
    @DisplayName("이메일 형식에 맞지 않을 시, 예외가 발생한다.")
    void signUpExceptionInValidEmailLength() {
        // given
        String email = "a".repeat(47) + "@a.a";

        // when, then
        Assertions.assertThatThrownBy(() -> new Member(email, "범고래", "12345678"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이메일 형식에 맞지 않습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"a:1", "a:20"}, delimiter = ':')
    @DisplayName("정상 닉네임을 입력 받아, 회원가입을 한다.")
    void signUpCorrectNickname(String nicknameChar, int count) {
        // given
        String nickname = nicknameChar.repeat(count);

        // when
        Member member = new Member("boem@naver.com", nickname, "password123");

        // then
        assertThat(member).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("닉네임 글자 수가 맞지 않을 시, 예외가 발생한다.")
    @CsvSource(value = {"a:0", "a:21"}, delimiter = ':')
    void signUpExceptionInValidNicknameForm(String nicknameChar, int count) {
        // given
        String nickname = nicknameChar.repeat(count);

        // when, then
        Assertions.assertThatThrownBy(() -> new Member("beomWhale@naver.com", nickname, "12345678"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("닉네임 형식에 맞지 않습니다.");
    }

    @Test
    @DisplayName("닉네임에 null 이 들어올시, 예외가 발생한다.")
    void signUpExceptionInvalidNicknameNull() {
        // when, then
        Assertions.assertThatThrownBy(() -> new Member("beomWhale@naver.com", null, "12345678"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("닉네임 형식에 맞지 않습니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"a:8", "a:20"}, delimiter = ':')
    @DisplayName("정상 비밀번호를 입력 받아, 회원가입을 한다.")
    void signUpCorrectPassword(String passwordChar, int count) {
        // given
        String password = passwordChar.repeat(count);

        // when
        Member member = new Member("boem@naver.com", "범고래", password);

        // then
        assertThat(member).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("비밀번호 글자 수가 맞지 않을 시, 예외가 발생한다.")
    @CsvSource(value = {"a:7", "a:21"}, delimiter = ':')
    void signUpExceptionInValidPasswordLength(String passwordChar, int count) {
        // given
        String password = passwordChar.repeat(count);

        // when, then
        Assertions.assertThatThrownBy(() -> new Member("beomWhale@naver.com", "범고래",
                password))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호 형식에 맞지 않습니다.");
    }
}
