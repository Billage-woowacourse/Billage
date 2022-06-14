package com.example.billage.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private final String value;
    private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9]+@[A-Za-z0-9.]+$");

    public Email(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateNull(value);
        validateEmailForm(value);
        validateLength(value);
    }

    private void validateLength(String value) {
        if (value.length() > 50) {
            throw new IllegalArgumentException("이메일 형식에 맞지 않습니다.");
        }
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("이메일 형식에 맞지 않습니다.");
        }
    }

    private void validateEmailForm(String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("이메일 형식에 맞지 않습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
