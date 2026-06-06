package com.myatos.net.issues.exception.jiraexception;

import com.myatos.net.issues.exception.CustomException;
import org.springframework.http.HttpStatus;

public class JiraApiException extends CustomException {
    public JiraApiException(String reason) {
        super(reason, HttpStatus.UNAUTHORIZED);
    }
}