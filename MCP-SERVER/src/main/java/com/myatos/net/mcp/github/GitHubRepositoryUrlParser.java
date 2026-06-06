package com.myatos.net.mcp.github;

import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class GitHubRepositoryUrlParser {

    public ParsedRepository parse(String repoUrl) {
        if (repoUrl == null || repoUrl.isBlank()) {
            throw new IllegalArgumentException("GitHub repository URL is required");
        }
        String value = repoUrl.trim();
        if (value.startsWith("git@")) {
            return parseSsh(value);
        }
        return parseHttp(value);
    }

    private ParsedRepository parseHttp(String value) {
        URI uri = URI.create(value);
        String path = trimGitSuffix(uri.getPath());
        String[] parts = path == null ? new String[0] : path.split("/");
        if (parts.length < 3) {
            throw new IllegalArgumentException("GitHub repository URL must include owner and repository: " + value);
        }
        return new ParsedRepository(parts[1], parts[2]);
    }

    private ParsedRepository parseSsh(String value) {
        int separator = value.indexOf(':');
        if (separator < 0 || separator == value.length() - 1) {
            throw new IllegalArgumentException("GitHub SSH repository URL must include owner and repository: " + value);
        }
        String[] parts = trimGitSuffix(value.substring(separator + 1)).split("/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("GitHub SSH repository URL must include owner and repository: " + value);
        }
        return new ParsedRepository(parts[0], parts[1]);
    }

    private String trimGitSuffix(String value) {
        if (value == null) {
            return "";
        }
        return value.endsWith(".git") ? value.substring(0, value.length() - ".git".length()) : value;
    }

    public record ParsedRepository(String owner, String repository) {
        public String fullName() {
            return owner + "/" + repository;
        }
    }
}
