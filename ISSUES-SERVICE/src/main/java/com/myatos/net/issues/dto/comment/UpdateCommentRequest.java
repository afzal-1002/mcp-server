package com.myatos.net.issues.dto.comment;

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
public class UpdateCommentRequest {
    private Body body;
    private Visibility visibility;
}
