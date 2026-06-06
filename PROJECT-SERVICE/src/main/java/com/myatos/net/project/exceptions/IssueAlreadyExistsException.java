package com.myatos.net.project.exceptions;

public class IssueAlreadyExistsException extends RuntimeException {

    public IssueAlreadyExistsException(String message) {
        super(message);
    }
}
