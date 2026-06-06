package com.myatos.net.mcp.dto;

public record JiraCopilotIssueRequest(
        String jiraDomain,
        String jiraIssueKey,
        String jiraEmail,
        String jiraApiToken,
        String owner,
        String repository,
        String githubToken,
        String targetRepository,
        String baseBranch,
        String customInstructions,
        String customAgent,
        String model
) {
}
