package com.myatos.net.issues.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VisibilityType {
    ROLE("role"),
    GROUP("group");

    private final String jiraValue;
}
