package com.example.billage.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Password {

    private String password;

    protected Password() {
    }

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        validateNull(password);
        validateLength(password);
    }

    private void validateLength(String password) {
        int length = password.length();
        if (length < 8 || length > 20) {
            throw new IllegalArgumentException("비밀번호 형식에 맞지 않습니다.");
        }
    }

    private void validateNull(String password) {
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("비밀번호 형식에 맞지 않습니다.");
        }
    }
}
