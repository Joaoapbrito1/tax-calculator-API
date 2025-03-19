package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.JwtResponseDTO;
import br.com.tax_calculator_API.dtos.UserLoginDTO;
import br.com.tax_calculator_API.infra.jwt.JwtTokenProvider;
import br.com.tax_calculator_API.repository.UserRepository;
import br.com.tax_calculator_API.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testLogin() {
        // Arrange
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername("testUser");
        userLoginDTO.setPassword("testPassword");

        String expectedToken = "mocked-jwt-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(expectedToken);

        // Act
        JwtResponseDTO response = authService.login(userLoginDTO);

        // Assert
        assertEquals(expectedToken, response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(authentication);
        verify(securityContext, times(1)).setAuthentication(authentication);
    }
}