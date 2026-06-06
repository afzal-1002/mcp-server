package com.myatos.net.mcp.github;

public record DynamicCopilotContext(
        String jiraBaseUrl,
        String jiraEmail,
        String jiraApiToken,
        String projectKey,
        String repositoryOwner,
        String repositoryName,
        String targetRepository,
        String baseBranch,
        String githubToken
) {
}
