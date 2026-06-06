package com.myatos.net.user.exception.jiraexception;

import com.myatos.net.user.exception.CustomException;
import org.springframework.http.HttpStatus;

public class JiraApiException extends CustomException {
    public JiraApiException(String reason) {
        super(reason, HttpStatus.UNAUTHORIZED);
    }
}