package sogorae.billage.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private static final Pattern PATTERN = Pattern.compile(
        "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    private String email;

    public Email(String value) {
        validate(value);
        this.email = value;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email1 = (Email)o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
