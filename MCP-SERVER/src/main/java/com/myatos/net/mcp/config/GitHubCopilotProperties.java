package com.myatos.net.mcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mcp.github")
public record GitHubCopilotProperties(
        String host,
        String apiBaseUrl,
        String owner,
        String repository,
        String baseBranch,
        String targetRepository,
        String customAgent,
        String model,
        String copilotAssignee,
        String apiVersion
) {
    public GitHubCopilotProperties {
        host = isBlank(host) ? "github.com" : host;
        baseBranch = isBlank(baseBranch) ? "main" : baseBranch;
        targetRepository = isBlank(targetRepository) && !isBlank(owner) && !isBlank(repository)
                ? owner + "/" + repository
                : targetRepository;
        copilotAssignee = isBlank(copilotAssignee) ? "copilot-swe-agent[bot]" : copilotAssignee;
        apiVersion = isBlank(apiVersion) ? "2022-11-28" : apiVersion;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
