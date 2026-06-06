package com.myatos.net.mcp.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ServiceHttpClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthHeaderProvider authHeaderProvider;

    public ServiceHttpClient(AuthHeaderProvider authHeaderProvider) {
        this.authHeaderProvider = authHeaderProvider;
    }

    public <T> T get(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(HttpHeaders.AUTHORIZATION, authHeaderProvider.currentAuthorizationHeader());
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType).getBody();
    }
}
