package com.myatos.net.user.service;

import com.myatos.net.user.dto.credentials.CreateIntegrationCredentialRequest;
import com.myatos.net.user.dto.credentials.IntegrationCredentialResponse;
import com.myatos.net.user.dto.credentials.ResolvedIntegrationCredentialResponse;
import com.myatos.net.user.dto.credentials.UpdateIntegrationCredentialRequest;
import com.myatos.net.user.enums.IntegrationCredentialType;

import java.util.List;

public interface IntegrationCredentialService {
    IntegrationCredentialResponse create(CreateIntegrationCredentialRequest request);
    IntegrationCredentialResponse update(Long credentialId, UpdateIntegrationCredentialRequest request);
    void delete(Long credentialId);
    IntegrationCredentialResponse get(Long credentialId);
    ResolvedIntegrationCredentialResponse getResolved(Long credentialId);
    List<IntegrationCredentialResponse> listMine(IntegrationCredentialType type);
}
