package com.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
public class OperationNotPermittedException extends RuntimeException{
    public OperationNotPermittedException(String message) {
        super(message);
    }
}
