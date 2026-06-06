package com.myatos.net.user.service;

import com.myatos.net.user.enums.Role;
import com.myatos.net.user.model.user.User;
import com.myatos.net.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private AuthService authService;

    @Test
    void verifyBasicAcceptsEmailLoginWhenPasswordMatches() {
        User user = User.builder()
                .id(6L)
                .username("01108500@pw.edu.pl")
                .emailAddress("01108500@pw.edu.pl")
                .password("$2a$10$hash")
                .accountId("712020:0306160e-f103-45d5-a2f4-b2d20c16e447")
                .roles(Set.of(Role.USER))
                .build();

        when(userRepository.findByUsername("01108500@pw.edu.pl")).thenReturn(java.util.Optional.of(user));
        when(encryptionService.matches("123456", "$2a$10$hash")).thenReturn(true);

        String basicHeader = "Basic " + Base64.getEncoder()
                .encodeToString("01108500@pw.edu.pl:123456".getBytes(StandardCharsets.UTF_8));

        var response = authService.verifyBasic(basicHeader);

        assertEquals(6L, response.userId());
        assertEquals("01108500@pw.edu.pl", response.username());
    }

    @Test
    void verifyBasicReturnsExpectedMessageWhenUsernameIsUnknown() {
        when(userRepository.findByUsername("unknown")).thenReturn(java.util.Optional.empty());

        String basicHeader = "Basic " + Base64.getEncoder()
                .encodeToString("unknown:123456".getBytes(StandardCharsets.UTF_8));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.verifyBasic(basicHeader));

        assertEquals("Invalid username or password", ex.getReason());
    }

    @Test
    void verifyBasicReturnsExpectedMessageWhenPasswordIsWrong() {
        User user = User.builder()
                .id(6L)
                .username("01108500@pw.edu.pl")
                .emailAddress("01108500@pw.edu.pl")
                .password("$2a$10$hash")
                .accountId("712020:0306160e-f103-45d5-a2f4-b2d20c16e447")
                .roles(Set.of(Role.USER))
                .build();

        when(userRepository.findByUsername("01108500@pw.edu.pl")).thenReturn(java.util.Optional.of(user));
        when(encryptionService.matches("wrong", "$2a$10$hash")).thenReturn(false);

        String basicHeader = "Basic " + Base64.getEncoder()
                .encodeToString("01108500@pw.edu.pl:wrong".getBytes(StandardCharsets.UTF_8));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.verifyBasic(basicHeader));

        assertEquals("Invalid username or password", ex.getReason());
    }
}
