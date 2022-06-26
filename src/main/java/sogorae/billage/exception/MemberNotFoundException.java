package sogorae.billage.exception;

public class MemberNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 회원이 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(Throwable cause) {
        this(MESSAGE, cause);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
