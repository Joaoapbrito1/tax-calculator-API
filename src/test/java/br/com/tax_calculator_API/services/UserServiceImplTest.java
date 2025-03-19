package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.UserRequestDTO;
import br.com.tax_calculator_API.enums.UserRole;
import br.com.tax_calculator_API.exeptions.UserAlreadyExistsException;
import br.com.tax_calculator_API.models.UserModel;
import br.com.tax_calculator_API.repository.UserRepository;
import br.com.tax_calculator_API.repository.UserRoleRepository;
import br.com.tax_calculator_API.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ROLE_USER);

        userRequestDTO = new UserRequestDTO("testuser", "password", roles);
    }

    @Test
    void registerSuccess() {
        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("encodedPassword");

        userService.registerUser(userRequestDTO);

        verify(roleRepository, times(1)).saveAll(anySet());
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    void usernameExists() {
        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.registerUser(userRequestDTO));

        verify(roleRepository, never()).saveAll(anySet());
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void runtimeException() {
        when(userRepository.existsByUsername(userRequestDTO.getUsername())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userRequestDTO));

        assertEquals("User already registered with the username: testuser", exception.getMessage());
    }
}