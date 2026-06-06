package com.myatos.net.project.model.common;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myatos.net.project.dto.user.UserSummary;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {
    private String id;
    private String self;
    private String filename;
    private UserSummary author;
    private OffsetDateTime created;
    private Long size;

    @JsonProperty("mimeType")
    private String mimeType;
    private String content;
    private String thumbnail;
}
