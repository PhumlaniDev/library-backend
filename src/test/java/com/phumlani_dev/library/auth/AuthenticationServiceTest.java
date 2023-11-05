package com.phumlani_dev.library.auth;

import com.phumlani_dev.library.config.JwtService;
import com.phumlani_dev.library.model.Role;
import com.phumlani_dev.library.model.User;
import com.phumlani_dev.library.repository.UserRepository;
import com.phumlani_dev.library.token.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;

    @BeforeEach
    void  setUp(){
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(
                        userRepository, tokenRepository, passwordEncoder, jwtService);
    }

    @Test
    void shouldRegisterUser() {
        RegisterRequest request = new RegisterRequest(
                "Phumlani",
                "Arendse",
                "phumlani@arendsetech.com",
                "12345678",
                Role.ADMIN
                );
        User user = User.builder().build();

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponse response =
                authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken",response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        Mockito.verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldAuthenticateUser() {

        AuthenticationRequest request = new AuthenticationRequest(
                "phumlani@arendsetech.com",
                "12345678"
        );

        User user = User.builder().build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertNull(response.getErrorMessage());
        assertEquals(HttpStatus.OK, response.getHttpStatus());

        Mockito.verify(userRepository).findByEmail("phumlani@arendsetech.com");
    }

    @Test
    void refreshToken() {
    }
}