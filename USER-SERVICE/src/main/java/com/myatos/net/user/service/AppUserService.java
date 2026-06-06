package com.myatos.net.user.service;


import com.myatos.net.user.dto.appuser.AuthUserDTO;
import com.myatos.net.user.model.user.AppUser;

import java.util.Optional;

public interface AppUserService {
    AppUser register(String username, String rawPassword, String roles);
    Optional<AppUser> findByUsername(String username);
    AuthUserDTO getByUsername(String username);
    boolean validatePassword(String username, String rawPassword);
    String getCurrentUsername();
    AppUser getCurrentUserOrThrow();
    public void assertPasswordMatches(String username, String rawPassword);
}
