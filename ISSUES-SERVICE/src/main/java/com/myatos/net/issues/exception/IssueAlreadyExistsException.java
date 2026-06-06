package com.myatos.net.issues.exception;

public class IssueAlreadyExistsException extends RuntimeException {

    public IssueAlreadyExistsException(String message) {
        super(message);
    }
}
