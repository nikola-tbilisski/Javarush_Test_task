package com.game.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //Not found err 404
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException() {
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
