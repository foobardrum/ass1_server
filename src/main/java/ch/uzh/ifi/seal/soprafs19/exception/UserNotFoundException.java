package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(long id) {
        super(HttpStatus.NOT_FOUND,"User with following Id not found: "+id);
    }
}
