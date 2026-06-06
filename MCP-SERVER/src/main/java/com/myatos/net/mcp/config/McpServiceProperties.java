package com.myatos.net.mcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mcp.services")
public record McpServiceProperties(
        String userServiceUrl,
        String projectServiceUrl
) {
    public McpServiceProperties {
        userServiceUrl = normalize(userServiceUrl, "http://localhost:8081");
        projectServiceUrl = normalize(projectServiceUrl, "http://localhost:8082");
    }

    private static String normalize(String value, String fallback) {
        String resolved = value == null || value.isBlank() ? fallback : value.trim();
        while (resolved.endsWith("/")) {
            resolved = resolved.substring(0, resolved.length() - 1);
        }
        return resolved;
    }
}
