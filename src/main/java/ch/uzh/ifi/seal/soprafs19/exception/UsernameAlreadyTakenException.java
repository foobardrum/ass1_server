package ch.uzh.ifi.seal.soprafs19.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Username already taken")
public class UsernameAlreadyTakenException extends RuntimeException {

    private final Logger log = LoggerFactory.getLogger(Exception.class);

    public UsernameAlreadyTakenException(String ErrorMessage) {
        super(ErrorMessage);
        log.debug(ErrorMessage);
    }
}
