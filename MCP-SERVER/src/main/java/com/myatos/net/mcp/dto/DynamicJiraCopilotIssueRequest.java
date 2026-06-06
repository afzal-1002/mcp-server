package com.myatos.net.mcp.dto;

public record DynamicJiraCopilotIssueRequest(
        String jiraBaseUrl,
        String projectKey,
        Long repositoryId,
        String repositoryUrl,
        String jiraIssueKey,
        String customInstructions,
        String customAgent,
        String model
) {
}
