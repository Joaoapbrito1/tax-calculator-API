package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserResponseDTO;
import br.com.tax_calculator_API.exeptions.UserInvalidDataException;
import br.com.tax_calculator_API.exeptions.UserResourceNotFoundException;
import br.com.tax_calculator_API.exeptions.UserNotAuthenticatedException;
import br.com.tax_calculator_API.infra.jwt.JwtTokenProvider;
import br.com.tax_calculator_API.services.impl.AuthUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthUserServiceImplTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private AuthUserServiceImpl authUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authUserService = new AuthUserServiceImpl(jwtTokenProvider);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getUserInfo_ShouldReturnUserInfo_WhenUserIsAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");

        UserResponseDTO response = null;
        try {
            response = authUserService.getUserInfo("dummyToken");
        } catch (UserNotAuthenticatedException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Welcome, testUser!", response.getMessage());
    }

    @Test
    void getUserInfo_ShouldThrowException_WhenUserIsNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(UserNotAuthenticatedException.class, () -> authUserService.getUserInfo("dummyToken"));
    }

    @Test
    void adminAccess_ShouldReturnAdminAccess_WhenUserHasNoRoleUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn((Collection) Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));

        String response = authUserService.adminAccess();

        assertEquals("Admin access", response);
    }

    @Test
    void adminAccess_ShouldThrowException_WhenUserHasRoleUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn((Collection) Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        assertThrows(UserInvalidDataException.class, () -> authUserService.adminAccess());
    }

    @Test
    void deleteResource_ShouldThrowException_WhenResourceDoesNotExist() {
        assertThrows(UserResourceNotFoundException.class, () -> authUserService.deleteResource(1L));
    }

    @Test
    void getUserInfo_ShouldThrowException_WhenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(UserNotAuthenticatedException.class, () -> authUserService.getUserInfo("dummyToken"));
    }

    @Test
    void getUserInfo_ShouldThrowException_WhenAuthenticationIsNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(UserNotAuthenticatedException.class, () -> authUserService.getUserInfo("dummyToken"));
    }
}