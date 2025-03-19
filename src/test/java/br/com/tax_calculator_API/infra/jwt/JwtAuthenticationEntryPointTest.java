package br.com.tax_calculator_API.infra.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
    }

    @Test
    void testCommence_ShouldSendUnauthorizedError() throws IOException {
        when(authException.getMessage()).thenReturn("Unauthorized");

        try {
            jwtAuthenticationEntryPoint.commence(request, response, authException);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}