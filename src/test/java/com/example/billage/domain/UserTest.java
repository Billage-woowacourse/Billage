package com.example.billage.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class UserTest {

    @ParameterizedTest
    @DisplayName("이메일 형식에 맞지 않을 시, 예외가 발생한다.")
    @ValueSource(strings = {"@naver.com", "boem", "boem@@naver.com"})
    @NullSource
    void signUpExceptionInValidEmailFormat(String email) {
        Assertions.assertThatThrownBy(() -> new User(email, "범고래", "12345678"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이메일 형식에 맞지 않습니다.");
    }

    @Test
    @DisplayName("이메일 형식에 맞지 않을 시, 예외가 발생한다.")
    void signUpExceptionInValidEmailLength() {
        String email = "a".repeat(48) + "@a.a";
        System.out.println(email);
        Assertions.assertThatThrownBy(() -> new User(email, "범고래", "12345678"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("이메일 형식에 맞지 않습니다.");
    }
}
