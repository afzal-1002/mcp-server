package com.myatos.net.project.exceptions.jiraexception;

import com.myatos.net.project.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidJiraCredentialException extends CustomException {
    public InvalidJiraCredentialException(String reason) {
        super(reason, HttpStatus.UNAUTHORIZED);
    }
}