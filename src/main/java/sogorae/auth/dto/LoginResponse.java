package sogorae.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
