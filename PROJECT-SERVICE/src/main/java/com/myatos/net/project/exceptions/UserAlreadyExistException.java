package com.myatos.net.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistException extends CustomException {
    public UserAlreadyExistException (String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
