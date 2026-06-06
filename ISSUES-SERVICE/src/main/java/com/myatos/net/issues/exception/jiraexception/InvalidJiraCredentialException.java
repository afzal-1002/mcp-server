package com.myatos.net.issues.exception.jiraexception;

import com.myatos.net.issues.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidJiraCredentialException extends CustomException {
    public InvalidJiraCredentialException(String reason) {
        super(reason, HttpStatus.UNAUTHORIZED);
    }
}