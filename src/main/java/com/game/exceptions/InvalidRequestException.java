package com.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //bad request 400 error code
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
