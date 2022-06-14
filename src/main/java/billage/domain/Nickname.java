package billage.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {

    private String nickname;

    public Nickname(String value) {
        validate(value);
        this.nickname = value;
    }

    private void validate(String value) {
        validateNull(value);
        validateLength(value);
    }

    private void validateLength(String value) {
        int length = value.length();
        if (length < 1 || length > 20) {
            throw new IllegalArgumentException("닉네임 형식에 맞지 않습니다.");
        }
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("닉네임 형식에 맞지 않습니다.");
        }
    }
}
