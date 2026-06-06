package com.myatos.net.user.dto.user;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public  class JiraUserDetailsRequest {
    private String baseURL;
    private String username;
    private String apiToken;
}