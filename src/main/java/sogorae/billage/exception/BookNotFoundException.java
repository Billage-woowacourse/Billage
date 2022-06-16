package sogorae.billage.exception;

public class BookNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 책이 존재하지 않습니다.";

    public BookNotFoundException() {
        super(MESSAGE);
    }
}
