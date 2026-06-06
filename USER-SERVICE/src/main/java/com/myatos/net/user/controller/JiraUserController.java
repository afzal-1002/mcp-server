package com.myatos.net.user.controller;

import com.myatos.net.user.dto.user.JiraUserMeResponse;
import com.myatos.net.user.dto.user.JiraUserResponse;
import com.myatos.net.user.service.JiraUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wut/jira-users")
@RequiredArgsConstructor
@Validated
public class JiraUserController {

    private final JiraUserService jiraUserService;

    @GetMapping("/me")
    public ResponseEntity<JiraUserResponse> me() {
        return ResponseEntity.ok(jiraUserService.getMyJiraProfile());
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<JiraUserMeResponse> byUsername(@PathVariable String username) {
        return ResponseEntity.ok(jiraUserService.findJiraUserByUserName(username));
    }

    @GetMapping("/by-username")
    public ResponseEntity<JiraUserResponse> getJiraUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok(jiraUserService.getJiraUserByUsername(username));
    }


}
