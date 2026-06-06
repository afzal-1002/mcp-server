package com.myatos.net.project.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateProjectKeyException extends CustomException {
    public DuplicateProjectKeyException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
