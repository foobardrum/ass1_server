package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthorizedException extends ResponseStatusException {
    public NotAuthorizedException() {
        super(HttpStatus.UNAUTHORIZED,"Provided token is unauthorized!");
    }
}
