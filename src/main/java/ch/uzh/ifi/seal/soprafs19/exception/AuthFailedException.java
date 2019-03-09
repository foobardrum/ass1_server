package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthFailedException extends ResponseStatusException {

    public AuthFailedException() {
        super(HttpStatus.NOT_ACCEPTABLE,"Invalid authentication data provided!");
    }
}
