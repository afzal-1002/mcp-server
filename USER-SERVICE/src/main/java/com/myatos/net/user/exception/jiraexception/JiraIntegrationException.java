package com.myatos.net.user.exception.jiraexception;

import com.myatos.net.user.exception.CustomException;
import org.springframework.http.HttpStatus;

public class JiraIntegrationException extends CustomException {

    public JiraIntegrationException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
