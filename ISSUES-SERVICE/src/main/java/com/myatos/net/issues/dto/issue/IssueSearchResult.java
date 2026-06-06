package com.myatos.net.issues.dto.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myatos.net.issues.dto.issue.response.Issuereponse.IssueResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueSearchResult {
    private String expand;
    private int startAt;
    private int maxResults;
    private int total;
    private List<IssueResponse> issues;
}