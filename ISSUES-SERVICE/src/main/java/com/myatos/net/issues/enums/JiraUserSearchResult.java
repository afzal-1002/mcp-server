package com.myatos.net.issues.enums;

import lombok.Data;

@Data
public class JiraUserSearchResult {
    private String accountId;
    private String displayName;
    private String emailAddress;
}
