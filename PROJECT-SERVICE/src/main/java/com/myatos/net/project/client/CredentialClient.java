package com.myatos.net.project.client;


import com.myatos.net.project.configuration.FeignSecurityConfiguration;
import com.myatos.net.project.dto.credentials.UserCredentialRequest;
import com.myatos.net.project.dto.credentials.UserCredentialResponse;
import com.myatos.net.project.dto.user.TokenRequest;
import com.myatos.net.project.dto.user.TokenResponse;
import com.myatos.net.project.dto.user.UserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//@FeignClient( name = "user-service",
//        contextId = "jiraCredentialClient",
//        url = "${user.service.url}",
//        path = "/api/wut/credentials",
//        configuration = FeignSecurityConfiguration.class
//)

@FeignClient(
        name = "USER-SERVICE",
        contextId = "jiraCredentialClient",
        path = "/api/wut/credentials",
        configuration = FeignSecurityConfiguration.class
)
public interface CredentialClient {

    // CREATE
    @PostMapping
    UserCredentialResponse addCredential(@RequestBody UserCredentialRequest credential);

    // READ (current)
    @GetMapping("/me")
    UserCredentialResponse getMine();

    // READ (utility)
    @GetMapping("/by-username")
    UserCredentialResponse getByUsername(@RequestParam("username") String username);

    @GetMapping("/{credentialId}")
    UserCredentialResponse getById(@PathVariable("credentialId") Long credentialId);

    @GetMapping("/user-summary/by-account-id")
    UserSummary getUserSummaryByAccountId(@RequestParam("accountId") String accountId);

    // UPDATE (current)
    @PutMapping("/base-url")
    UserCredentialResponse updateBaseURLForCurrentUser(@RequestParam("oldURL") String oldURL,
                                                       @RequestParam("newURL") String newURL);

    @PutMapping("/token")
    UserCredentialResponse updateToken(@RequestBody TokenRequest request);

    // DELETE
    @DeleteMapping("/me")
    UserCredentialResponse deleteMine();

    @DeleteMapping("/by-username")
    UserCredentialResponse deleteByUsername(@RequestParam("username") String username);

    // EXISTENCE
    @GetMapping("/exists/username")
    Boolean existsByJiraUsername(@RequestParam("username") String username);

    @GetMapping("/exists/account-id")
    Boolean existsByAccountId(@RequestParam("accountId") String accountId);

    // TOKEN HELPERS
    @PostMapping("/encrypt/token")
    TokenResponse encryptToken(@RequestBody TokenRequest request);

    @PostMapping("/decrypt/token")
    TokenResponse decryptToken(@RequestBody TokenRequest request);
}