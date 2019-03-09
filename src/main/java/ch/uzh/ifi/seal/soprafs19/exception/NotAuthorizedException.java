package ch.uzh.ifi.seal.soprafs19.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Provided Token is unauthorized!")
public class NotAuthorizedException extends RuntimeException {

    private final Logger log = LoggerFactory.getLogger(Exception.class);

    public NotAuthorizedException(String ErrorMessage) {
        super(ErrorMessage);
        log.debug(ErrorMessage);
    }
}
