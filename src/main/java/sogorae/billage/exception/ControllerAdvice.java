package sogorae.billage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sogorae.auth.exception.InvalidTokenException;
import sogorae.auth.exception.LoginFailException;

//@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({LoginFailException.class, MemberNotFoundException.class,
      IllegalArgumentException.class, MemberDuplicationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidTokenException(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
