package com.myatos.net.mcp.client.dto;

public record ResolvedIntegrationCredentialResponse(
        Long id,
        String name,
        String type,
        String username,
        String secret,
        String secretReference
) {
}
