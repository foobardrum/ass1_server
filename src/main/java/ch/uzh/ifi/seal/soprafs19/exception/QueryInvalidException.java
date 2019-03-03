package ch.uzh.ifi.seal.soprafs19.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Invalid Query")
public class QueryInvalidException extends RuntimeException {

    private final Logger log = LoggerFactory.getLogger(Exception.class);

    public QueryInvalidException(String ErrorMessage) {
        super(ErrorMessage);
        log.info(ErrorMessage);
    }
}
