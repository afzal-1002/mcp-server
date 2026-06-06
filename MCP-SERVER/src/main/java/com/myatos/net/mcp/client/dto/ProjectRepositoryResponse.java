package com.myatos.net.mcp.client.dto;

public record ProjectRepositoryResponse(
        Long id,
        String baseUrl,
        String projectKey,
        String repoName,
        String repoUrl,
        String defaultBranch,
        Long credentialId,
        boolean primaryRepository,
        boolean active
) {
}
