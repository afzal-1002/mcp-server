package com.myatos.net.user.controller;

import com.myatos.net.user.dto.appuser.AuthUserDTO;
import com.myatos.net.user.mapper.AuthUserMapper;
import com.myatos.net.user.model.user.AppUser;
import com.myatos.net.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wut/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final AppUserService appUserService;

    @GetMapping("/me")
    public ResponseEntity<AuthUserDTO> getCurrentUserProfile() {
        AppUser user = appUserService.getCurrentUserOrThrow();
        return ResponseEntity.ok(AuthUserMapper.toAuthUserDTO(user));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<AuthUserDTO> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(appUserService.getByUsername(username));
    }
}
