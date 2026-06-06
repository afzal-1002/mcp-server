package com.myatos.net.mcp.client.dto;

public record UserCredentialResponse(
        Long userId,
        String username,
        String accountId,
        String token,
        String baseUrl
) {
}
