package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserResponseDTO;
import br.com.tax_calculator_API.infra.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceImplTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthUserServiceImpl authUserService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void authenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");

        UserResponseDTO response = authUserService.getUserInfo("token");

        assertNotNull(response);
        assertEquals("Bem-vindo, testUser!", response.getMessage());
    }

    @Test
    void unauthenticatedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authUserService.getUserInfo("token"));
        assertEquals("Usuário não autenticado", exception.getMessage());
    }

    @Test
    void adminAccess() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());

        String response = authUserService.adminAccess();

        assertEquals("Acesso admin", response);
    }

    @Test
    void accessDenied() {
        GrantedAuthority userRole = () -> "ROLE_USER";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of(userRole));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authUserService.adminAccess());
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("403 FORBIDDEN \"Acesso negado para usuários com o papel ROLE_USER\"", exception.getMessage());
    }
}