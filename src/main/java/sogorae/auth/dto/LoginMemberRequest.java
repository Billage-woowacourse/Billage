package sogorae.auth.dto;

public class LoginMemberRequest {

    private final String email;
    private final String password;

    public LoginMemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
