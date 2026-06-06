package com.myatos.net.mcp.dto;

public record ProjectRepositoriesRequest(
        String jiraBaseUrl,
        String projectKey
) {
}
