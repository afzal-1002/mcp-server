package com.myatos.net.user.service;

import com.myatos.net.user.dto.user.TokenRequest;
import com.myatos.net.user.dto.user.TokenResponse;
import com.myatos.net.user.dto.credentials.UserCredentialRequest;
import com.myatos.net.user.dto.credentials.UserCredentialResponse;
import com.myatos.net.user.model.user.AppUser;
import com.myatos.net.user.model.user.User;
import com.myatos.net.user.model.user.UserCredential;
import com.myatos.net.user.dto.user.UserSummary;
import org.springframework.stereotype.Service;

@Service
public interface CredentialService {

    UserCredentialResponse addCredential(UserCredentialRequest credential);
    UserCredential getUserCredential(String username);
    UserCredentialResponse getCredentialByUserName(String username);
    UserCredentialResponse findByCredentialId(Long credentialId);
    UserCredentialResponse updateBaseUrlForCurrentUser(String username, String oldUrl, String newUrl);
    UserCredentialResponse deleteCredential(String username);
    boolean existsByJiraUsername(String jiraUsername);
    boolean existsByAccountId(String accountId);
    String findAccountIdByUsername(String username);
    UserCredentialResponse updateCredentialToken(String username, String newPlainToken);
    UserSummary findByAccountId(String accountId);

    TokenResponse decryptToken(TokenRequest encryptedToken);
    TokenResponse encryptToken(TokenRequest plainToken);

    UserCredential getForCurrentUserOrThrow();
    UserCredential getByUsernameOrThrow(String username);
    UserCredentialResponse getResolvedCredentialForCurrentUser(Long siteId, String baseUrl);

    UserCredential addCredentialAndLinkToUsers(UserCredentialRequest req, User user, AppUser appUser);
}
