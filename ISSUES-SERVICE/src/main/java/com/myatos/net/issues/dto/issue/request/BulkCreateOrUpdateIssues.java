package com.myatos.net.issues.dto.issue.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myatos.net.issues.dto.issue.CreateIssueRequest;
import lombok.Data;

import java.util.List;

@Data
public class BulkCreateOrUpdateIssues {
    @JsonProperty("issues")
    private List<CreateIssueRequest> createOrUpdateIssues;
}