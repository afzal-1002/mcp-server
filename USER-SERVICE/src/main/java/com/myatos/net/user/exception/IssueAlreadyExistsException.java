package com.myatos.net.user.exception;

public class IssueAlreadyExistsException extends RuntimeException {

    public IssueAlreadyExistsException(String message) {
        super(message);
    }
}
