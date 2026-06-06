package com.myatos.net.project.exceptions.jiraexception;

import com.myatos.net.project.exceptions.CustomException;
import org.springframework.http.HttpStatus;

public class JiraIntegrationException extends CustomException {

    public JiraIntegrationException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
