package com.myatos.net.mcp.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record ConfiguredJiraApiCallRequest(
        String baseUrl,
        String method,
        String endpoint,
        String email,
        String apiToken,
        JsonNode body,
        Object[] pathArgs
) {
}
