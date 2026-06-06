package com.myatos.net.user.dto.credentials;

import com.myatos.net.user.enums.IntegrationCredentialType;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ResolvedIntegrationCredentialResponse {
    private Long id;
    private String name;
    private IntegrationCredentialType type;
    private String username;
    private String secret;
    private String secretReference;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
