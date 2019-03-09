package ch.uzh.ifi.seal.soprafs19.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthorizedException extends ResponseStatusException {
    public NotAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED,"Provided token is unauthorized!");
    }
}
