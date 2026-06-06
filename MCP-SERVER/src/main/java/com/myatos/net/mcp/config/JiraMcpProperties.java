package com.myatos.net.mcp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mcp.jira")
public record JiraMcpProperties(
        String apiVersion,
        int defaultMaxResults
) {
    public JiraMcpProperties {
        apiVersion = isBlank(apiVersion) ? "3" : apiVersion;
        defaultMaxResults = defaultMaxResults <= 0 ? 25 : defaultMaxResults;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
