package com.myatos.net.issues.exception;

import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends CustomException {

    public UserNotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
