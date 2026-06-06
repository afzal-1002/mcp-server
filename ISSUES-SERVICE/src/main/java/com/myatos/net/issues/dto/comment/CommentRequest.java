package com.myatos.net.issues.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.myatos.net.issues.dto.issue.response.Issuereponse.Body;
import com.myatos.net.issues.dto.issue.response.Issuereponse.Visibility;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    private Body body;
    private Visibility visibility;
    @JsonProperty("public")
    private Boolean    jsdPublic;
}