package com.myatos.net.mcp.dto;

public record CopilotAssignRequest(
        String owner,
        String repository,
        Integer issueNumber,
        String githubToken,
        String targetRepository,
        String baseBranch,
        String customInstructions,
        String customAgent,
        String model
) {
}
