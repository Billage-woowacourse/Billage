package sogorae.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sogorae.billage.domain.Member;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMember {

    private String email;
    private String nickname;
    private String password;

    public static LoginMember from(Member member) {
        return new LoginMember(member.getEmail(), member.getNickname(), member.getPassword());
    }
}
