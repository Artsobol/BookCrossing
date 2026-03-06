package io.github.artsobol.bookcrossing.exception.security;

import io.github.artsobol.bookcrossing.exception.base.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class AuthenticationException extends BaseException {
    public AuthenticationException(String messageKey, Object... messageArgs) {
        super(messageKey, messageKey, HttpStatus.UNAUTHORIZED, Map.of(), null, messageArgs);
    }
}
