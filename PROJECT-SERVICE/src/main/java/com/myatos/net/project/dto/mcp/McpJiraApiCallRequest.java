package com.myatos.net.project.dto.mcp;

import com.fasterxml.jackson.databind.JsonNode;

public record McpJiraApiCallRequest(
        String domain,
        String method,
        String apiPath,
        String email,
        String apiToken,
        JsonNode body
) {
}
