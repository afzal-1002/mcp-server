package com.myatos.net.issues.dto.project;

import lombok.Data;

@Data
public  class ListProjectsRequest {
    private String username;
    private String baseUrl;
}