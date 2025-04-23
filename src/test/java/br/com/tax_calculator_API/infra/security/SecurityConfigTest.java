//package br.com.tax_calculator_API.infra.security;
//
//import br.com.tax_calculator_API.infra.jwt.JwtAuthenticationEntryPoint;
//import br.com.tax_calculator_API.infra.jwt.JwtAuthenticationFilter;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@SpringBootTest
//@AutoConfigureMockMvc
//class SecurityConfigTest {
//
//    @InjectMocks
//    private SecurityConfig securityConfig;
//
//    @Mock
//    private JwtAuthenticationFilter authenticationFilter;
//
//    @Mock
//    private JwtAuthenticationEntryPoint authenticationEntryPoint;
//
//    @Mock
//    private AccessDeniedHandler accessDeniedHandler;
//
//    @Mock
//    private AuthenticationConfiguration authenticationConfiguration;
//
//    @Mock
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private SecurityFilterChain securityFilterChain;
//
//    @Test
//    void testSecurityFilterChain() {
//        assertNotNull(securityFilterChain);
//    }
//
//    @Test
//    void testPasswordEncoder() {
//        BCryptPasswordEncoder passwordEncoder = (BCryptPasswordEncoder) SecurityConfig.passwordEncoder();
//        assertNotNull(passwordEncoder);
//        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
//    }
//
//    @Test
//    void testAuthenticationManager() throws Exception {
//        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
//        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
//
//        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);
//
//        verify(authenticationConfiguration).getAuthenticationManager();
//        assertEquals(authenticationManager, result);
//    }
//}
package br.com.tax_calculator_API.infra.security;

import br.com.tax_calculator_API.infra.jwt.JwtAuthenticationEntryPoint;
import br.com.tax_calculator_API.infra.jwt.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private JwtAuthenticationFilter authenticationFilter;

    @Mock
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    private AccessDeniedHandler accessDeniedHandler;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPasswordEncoder() {
        var encoder = SecurityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);

        verify(authenticationConfiguration).getAuthenticationManager();
        assertEquals(authenticationManager, result);
    }
}