package com.myatos.net.issues.exception;


import org.springframework.http.HttpStatus;

public class BugNotFoundException extends CustomException {

    public BugNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
