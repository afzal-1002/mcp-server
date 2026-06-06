package com.myatos.net.project.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myatos.net.project.enums.Role;
import com.myatos.net.project.model.common.AvatarUrls;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
import lombok.*;


import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummary {
    private String self;
    private String username;
    private Long   id;
    private String accountId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String displayName;
    private Boolean active;
    private String timeZone;

    @Embedded
    private AvatarUrls avatarUrls;

    @Transient
    private String jiraBaseUrl;
    @Transient private String phoneNumber;
    @Transient private List<Role> roles;
}

