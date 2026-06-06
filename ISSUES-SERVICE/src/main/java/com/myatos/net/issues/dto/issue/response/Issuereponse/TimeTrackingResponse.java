package com.myatos.net.issues.dto.issue.response.Issuereponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeTrackingResponse {
    private String originalEstimate;
    private String remainingEstimate;
    private Integer originalEstimateSeconds;
    private Integer remainingEstimateSeconds;
}

