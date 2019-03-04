package ch.uzh.ifi.seal.soprafs19.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="Invalid Authentication Data Provided")
public class AuthFailedException extends RuntimeException {

    private final Logger log = LoggerFactory.getLogger(Exception.class);

    public AuthFailedException(String ErrorMessage) {
        super(ErrorMessage);
        log.debug(ErrorMessage);
    }
}
