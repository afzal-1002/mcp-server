package com.myatos.net.issues.mapper;


import com.myatos.net.issues.dto.issue.response.Issuereponse.StatusCategory;
import com.myatos.net.issues.dto.issue.response.Issuereponse.StatusResponse;
import com.myatos.net.issues.dto.issuestatus.Scope;
import com.myatos.net.issues.dto.issuestatus.ScopeProject;
import com.myatos.net.issues.model.status.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public Status mapFromJira(StatusResponse dto) {
        return mapFromJira(dto, null);
    }

    public Status mapFromJira(StatusResponse statusResponse, Long ignoredProjectId) {
        if (statusResponse == null) {
            return null;
        }

        // Build the statusResponse entity
        Status.StatusBuilder statusBuilder = Status.builder()
                .name(statusResponse.getName())
                .description(statusResponse.getDescription())
                .iconUrl(statusResponse.getIconUrl());

        // Map statusResponse category
        if (statusResponse.getStatusCategory() != null) {
            statusBuilder.category(StatusCategory.builder()
                    .id(statusResponse.getStatusCategory().getId())
                    .key(statusResponse.getStatusCategory().getKey())
                    .name(statusResponse.getStatusCategory().getName())
                    .colorName(statusResponse.getStatusCategory().getColorName())
                    .build());
        }

        // Map scope if provided
        if (statusResponse.getScope() != null) {
            statusBuilder.scope(Scope.builder()
                    .type(statusResponse.getScope().getType())
                    .project(statusResponse.getScope().getProject() != null
                            ? ScopeProject.builder()
                            .id(statusResponse.getScope().getProject().getId())
                            .build()
                            : null)
                    .build());
        }

        return statusBuilder.build();
    }
}
