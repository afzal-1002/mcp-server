package com.myatos.net.issues.dto.project;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProjectSearchResponse {
    private Integer startAt;
    private Integer maxResults;
    private Integer total;
    private List<Map<String, Object>> values;
}
