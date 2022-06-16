package sogorae.billage.exception;

public class MemberDuplicationException extends RuntimeException {

    private static final String MESSAGE = "이미 존재하는 회원입니다.";

    public MemberDuplicationException() {
        super(MESSAGE);
    }
}
