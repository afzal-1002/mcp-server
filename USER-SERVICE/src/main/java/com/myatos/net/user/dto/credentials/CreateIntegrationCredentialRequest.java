package com.myatos.net.user.dto.credentials;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.myatos.net.user.enums.IntegrationCredentialType;
import lombok.Data;

@Data
public class CreateIntegrationCredentialRequest {
    private String name;
    private IntegrationCredentialType type;
    private String username;
    @JsonAlias("token")
    private String secret;
    private String secretReference;
}
