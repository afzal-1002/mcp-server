package com.myatos.net.user.mapper;

import com.myatos.net.user.dto.appuser.AuthUserDTO;
import com.myatos.net.user.model.user.AppUser;

public class AuthUserMapper {
    public static AuthUserDTO toAuthUserDTO(AppUser u) {
        return AuthUserDTO.builder()
                .username(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRoles())
                .build();
    }
}