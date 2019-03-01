package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No User with such Id")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super("Following Id not found: "+id);
    }
}
