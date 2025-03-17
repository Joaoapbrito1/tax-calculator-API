package br.com.tax_calculator_API.infra.jwt;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = "dGVzdFNlY3JldEtleVRlc3RTZWNyZXRLZXlUZXN0U2VjcmV0S2V5"; // Base64 encoded key
        jwtTokenProvider.jwtExpirationDate = 3600000; // 1 hora
    }

    @Test
    void testGenerateToken() {
        when(authentication.getName()).thenReturn("testUser");
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token, "Token should not be null");
        String username = jwtTokenProvider.getUsername(token);
        assertEquals("testUser", username, "Username should match");
    }

    @Test
    void testGetUsername() {
        when(authentication.getName()).thenReturn("testUser");
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        String token = jwtTokenProvider.generateToken(authentication);
        String username = jwtTokenProvider.getUsername(token);

        assertEquals("testUser", username, "Username should match");
    }

    @Test
    void testValidateToken() {
        when(authentication.getName()).thenReturn("testUser");
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        String token = jwtTokenProvider.generateToken(authentication);
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void testValidateToken_InvalidToken() {

        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYw9456000LCJleHAiOjE2MDk0OTYwMDB9.invalidSignature";
        assertThrows(SignatureException.class, () -> jwtTokenProvider.validateToken(invalidToken), "Invalid token should throw SignatureException");
    }
}