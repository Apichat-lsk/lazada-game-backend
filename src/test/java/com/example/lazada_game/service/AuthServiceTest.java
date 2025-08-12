package com.example.lazada_game.service;

import com.example.lazada_game.application.AuthService;
import com.example.lazada_game.domain.model.Users;
import com.example.lazada_game.domain.repository.AuthRepository;
import com.example.lazada_game.web.dto.ChangePasswordRequest;
import com.example.lazada_game.web.dto.JwtResponse;
import com.example.lazada_game.web.dto.LoginRequest;
import com.example.lazada_game.web.dto.RegisterResponse;
import com.example.lazada_game.web.jwtverify.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_whenEmailExists_shouldReturnFalseMessage() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setUsername("testuser");

        when(authRepository.existsByEmail(user.getEmail())).thenReturn(true);

        RegisterResponse response = authService.register(user);

        assertFalse(response.getCheck());
        assertEquals("Email already exists", response.getMessage());
    }

    @Test
    void register_whenUsernameExists_shouldReturnFalseMessage() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setUsername("testuser");

        when(authRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(authRepository.existsByUsername(user.getUsername())).thenReturn(true);

        RegisterResponse response = authService.register(user);

        assertFalse(response.getCheck());
        assertEquals("Username already exists", response.getMessage());
    }

    @Test
    void register_whenEmailAndUsernameNotExists_shouldReturnSuccess() {
        Users user = new Users();
        user.setEmail("test@example.com");
        user.setUsername("testuser");

        when(authRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(authRepository.existsByUsername(user.getUsername())).thenReturn(false);

        RegisterResponse response = authService.register(user);

        assertTrue(response.getCheck());
        assertEquals("Send OTP Success", response.getMessage());
    }

    @Test
    void login_whenUserNotFound_shouldReturnInvalidMessage() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("pass");

        when(authRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.empty());

        JwtResponse response = authService.login(loginRequest);

        assertFalse(response.getStatus());
        assertEquals("Invalid username or password", response.getMessage());
    }

    @Test
    void login_whenPasswordMismatch_shouldReturnInvalidMessage() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("wrongpass");

        Users user = new Users();
        user.setPassword("encodedpass");

        when(authRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        JwtResponse response = authService.login(loginRequest);

        assertFalse(response.getStatus());
        assertEquals("Invalid username or password", response.getMessage());
    }

    @Test
    void login_whenSuccess_shouldReturnToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("correctpass");

        Users user = new Users();
        user.setPassword("encodedpass");

        when(authRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("jwt-token");

        JwtResponse response = authService.login(loginRequest);

        assertTrue(response.getStatus());
        assertEquals("Login successfully", response.getMessage());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void changePassword_whenUserNotFound_shouldThrowException() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail("test@example.com");
        request.setPassword("newpass");

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.changePassword(request));
    }

    @Test
    void changePassword_whenUserFound_shouldUpdatePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail("test@example.com");
        request.setPassword("newpass");

        Users user = new Users();
        user.setEmail(request.getEmail());

        when(authRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedpass");
        when(authRepository.updateByEmail(any())).thenReturn(user);

        Users updatedUser = authService.changePassword(request);

        assertEquals("encodedpass", updatedUser.getPassword());
    }
}
