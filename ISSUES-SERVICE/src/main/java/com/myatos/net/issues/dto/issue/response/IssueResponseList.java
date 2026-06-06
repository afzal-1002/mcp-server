package com.myatos.net.issues.dto.issue.response;


import com.myatos.net.issues.dto.issue.response.Issuereponse.IssueResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response from POST /issue/bulkfetch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponseList {
    private List<IssueResponse> issues;
}

