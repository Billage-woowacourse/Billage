package sogorae.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sogorae.billage.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginMember {

    private String email;
    private String nickname;
    private String password;

    public static LoginMember from(Member member) {
        return new LoginMember(member.getEmail(), member.getNickname(), member.getPassword());
    }
}
