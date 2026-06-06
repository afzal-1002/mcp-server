package com.myatos.net.issues.dto.comment;

import com.myatos.net.issues.dto.issue.response.Issuereponse.CommentResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommentListEnvelope {
    private List<CommentResponse> comments; // in input order
    private List<String> missingIds;        // input ids that Jira didn’t return
}
