package sogorae.auth.exception;

public class LoginFailException extends RuntimeException {

    private static final String MESSAGE = "로그인에 실패했습니다.";

    public LoginFailException(Throwable cause) {
        this(MESSAGE, cause);
    }

    public LoginFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
