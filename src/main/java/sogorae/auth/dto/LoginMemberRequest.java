package sogorae.auth.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberRequest {

    private final String email;
    private final String password;
}
