package com.myatos.net.mcp.jira;

import com.myatos.net.mcp.config.JiraMcpProperties;
import org.springframework.stereotype.Component;

@Component
public class JiraUrlFactory {

    private final JiraMcpProperties properties;

    public JiraUrlFactory(JiraMcpProperties properties) {
        this.properties = properties;
    }

    public String genericUrl(String domainOrBaseUrl, String apiPath) {
        String baseUrl = normalizeBaseUrl(domainOrBaseUrl);
        String normalizedPath = normalizeApiPath(apiPath);
        if (normalizedPath.startsWith("/rest/api/")) {
            return baseUrl + normalizedPath;
        }
        return baseUrl + "/rest/api/" + properties.apiVersion() + normalizedPath;
    }

    public String configuredUrl(String domainOrBaseUrl, JiraApiEndpoint endpoint, Object... pathArgs) {
        return endpoint.buildUrl(normalizeBaseUrl(domainOrBaseUrl), properties.apiVersion(), pathArgs);
    }

    public String normalizeBaseUrl(String domainOrBaseUrl) {
        if (isBlank(domainOrBaseUrl)) {
            throw new IllegalArgumentException("Jira domain or base URL is required");
        }
        String value = domainOrBaseUrl.trim();
        if (!value.startsWith("http://") && !value.startsWith("https://")) {
            value = "https://" + value;
        }
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String normalizeApiPath(String apiPath) {
        if (isBlank(apiPath)) {
            throw new IllegalArgumentException("Jira API path is required");
        }
        String value = apiPath.trim();
        return value.startsWith("/") ? value : "/" + value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
