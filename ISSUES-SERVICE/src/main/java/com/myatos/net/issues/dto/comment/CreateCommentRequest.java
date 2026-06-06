package com.myatos.net.issues.dto.comment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myatos.net.issues.dto.issue.response.Issuereponse.Body;
import com.myatos.net.issues.dto.issue.response.Issuereponse.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCommentRequest {
    private Body body;
    private Visibility visibility;

    @JsonAlias({ "public" })
    @JsonProperty("public")
    private Boolean isPublic;
}