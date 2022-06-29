package sogorae.billage.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberSignUpRequest {

    private String email;
    private String nickname;
    private String password;

    public MemberSignUpRequest(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.password = "social_password";
    }
}
