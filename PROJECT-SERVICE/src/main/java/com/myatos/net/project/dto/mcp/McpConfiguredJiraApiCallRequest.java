package com.myatos.net.project.dto.mcp;

import com.fasterxml.jackson.databind.JsonNode;

public record McpConfiguredJiraApiCallRequest(
        String baseUrl,
        String method,
        String endpoint,
        String email,
        String apiToken,
        JsonNode body,
        Object[] pathArgs
) {
}
