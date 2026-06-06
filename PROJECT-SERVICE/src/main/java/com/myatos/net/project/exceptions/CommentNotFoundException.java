package com.myatos.net.project.exceptions;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CustomException {

    public CommentNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }

}
