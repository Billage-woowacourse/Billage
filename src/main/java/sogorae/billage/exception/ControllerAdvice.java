package sogorae.billage.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sogorae.auth.exception.InvalidTokenException;
import sogorae.auth.exception.LoginFailException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({LoginFailException.class, MemberNotFoundException.class,
      IllegalArgumentException.class, MemberDuplicationException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(RuntimeException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.error(message);
        return new ErrorResponse(message);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(RuntimeException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.error(message);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidTokenException(RuntimeException e) {
        String message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        log.error(message);
        return new ErrorResponse(e.getMessage());
    }
}
