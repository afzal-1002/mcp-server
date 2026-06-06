package com.myatos.net.issues.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.myatos.net.issues.dto.issue.response.Issuereponse.CommentResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter @ToString @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public   class CommentListResult {
    private int total;
    private int startAt;
    private int maxResults;

    @JsonProperty("comments")
    private List<CommentResponse> comments;
}