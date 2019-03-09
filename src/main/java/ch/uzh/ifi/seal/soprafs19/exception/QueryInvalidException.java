package ch.uzh.ifi.seal.soprafs19.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class QueryInvalidException extends ResponseStatusException {

    public QueryInvalidException(String ErrorMessage) {
        super(HttpStatus.BAD_REQUEST,"Query Invalid: "+ErrorMessage);
    }
}
