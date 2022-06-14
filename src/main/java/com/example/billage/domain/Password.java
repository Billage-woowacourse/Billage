package com.example.billage.domain;

import java.util.Objects;

public class Password {

    private final String value;

    public Password(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateNull(value);
        validateLength(value);
    }


    private void validateLength(String value) {
        int length = value.length();
        if (length < 8 || length > 20) {
            throw new IllegalArgumentException("비밀번호 형식에 맞지 않습니다.");
        }
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("비밀번호 형식에 맞지 않습니다.");
        }
    }


    public String getValue() {
        return value;
    }
}
