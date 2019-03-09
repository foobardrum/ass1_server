package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameAlreadyTakenException extends ResponseStatusException {

    public UsernameAlreadyTakenException(String username) {
        super(HttpStatus.CONFLICT,"username "+username+" already taken.");
    }
}
