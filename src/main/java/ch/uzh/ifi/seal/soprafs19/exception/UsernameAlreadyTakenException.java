package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Username already taken")
public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String ErrorMessage) {
        super(ErrorMessage);
    }
}
