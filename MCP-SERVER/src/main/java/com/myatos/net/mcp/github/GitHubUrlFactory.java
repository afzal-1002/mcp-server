package com.myatos.net.mcp.github;

import com.myatos.net.mcp.config.GitHubCopilotProperties;
import org.springframework.stereotype.Component;

@Component
public class GitHubUrlFactory {

    private final GitHubCopilotProperties properties;

    public GitHubUrlFactory(GitHubCopilotProperties properties) {
        this.properties = properties;
    }

    public String restUrl(String path) {
        return apiBaseUrl() + normalizePath(path);
    }

    public String graphqlUrl() {
        String apiBaseUrl = apiBaseUrl();
        if (apiBaseUrl.equals("https://api.github.com")) {
            return "https://api.github.com/graphql";
        }
        if (apiBaseUrl.endsWith("/api/v3")) {
            return apiBaseUrl.substring(0, apiBaseUrl.length() - "/api/v3".length()) + "/api/graphql";
        }
        return apiBaseUrl + "/graphql";
    }

    public String repositoryWebUrl(String owner, String repository) {
        return webBaseUrl() + "/" + owner + "/" + repository;
    }

    private String apiBaseUrl() {
        if (!isBlank(properties.apiBaseUrl())) {
            return trimTrailingSlash(properties.apiBaseUrl().trim());
        }
        String host = trimScheme(properties.host());
        if ("github.com".equalsIgnoreCase(host)) {
            return "https://api.github.com";
        }
        return "https://" + host + "/api/v3";
    }

    private String webBaseUrl() {
        String host = trimScheme(properties.host());
        return "https://" + host;
    }

    private String normalizePath(String path) {
        if (isBlank(path)) {
            throw new IllegalArgumentException("GitHub API path is required");
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    private String trimScheme(String value) {
        String trimmed = isBlank(value) ? "github.com" : value.trim();
        if (trimmed.startsWith("https://")) {
            trimmed = trimmed.substring("https://".length());
        }
        if (trimmed.startsWith("http://")) {
            trimmed = trimmed.substring("http://".length());
        }
        return trimTrailingSlash(trimmed);
    }

    private String trimTrailingSlash(String value) {
        String result = value;
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
