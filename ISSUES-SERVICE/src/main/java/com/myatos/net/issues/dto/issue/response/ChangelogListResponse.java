package com.myatos.net.issues.dto.issue.response;


import lombok.Data;

import java.util.List;

/**
 * Response from POST /issue/{id}/changelog/list
 */
@Data
public class ChangelogListResponse {
    private List<ChangelogResponse.ChangelogItem> values;
}

