package com.myatos.net.issues.configuration;


import com.myatos.net.issues.client.CredentialClient;
import com.myatos.net.issues.dto.credentials.UserCredentialResponse;
import com.myatos.net.issues.dto.helper.JiraUrlBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class CurrentCredSupport {

    private final CredentialClient credentialClient;
    private final JiraUrlBuilder jiraUrlBuilder;

    protected UserCredentialResponse currentCred() {
        UserCredentialResponse userCredential = credentialClient.getMine();
        if (userCredential == null || isBlank(userCredential.getBaseUrl())
                || isBlank(userCredential.getToken()) || isBlank(userCredential.getUsername())) {
            throw new IllegalStateException("Missing Jira credential for current user.");
        }
        userCredential.setBaseUrl(jiraUrlBuilder.normalizeJiraBaseUrl(userCredential.getBaseUrl()));
        return userCredential;
    }

    protected static boolean isBlank(String string){ return string == null || string.isBlank(); }
}
