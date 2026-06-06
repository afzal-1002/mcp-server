package com.myatos.net.mcp.dto;

public record CopilotIssueRequest(
        String owner,
        String repository,
        String title,
        String body,
        String githubToken,
        String targetRepository,
        String baseBranch,
        String customInstructions,
        String customAgent,
        String model
) {
}
