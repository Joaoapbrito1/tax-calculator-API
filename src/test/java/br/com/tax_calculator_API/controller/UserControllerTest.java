package br.com.tax_calculator_API.controller;

import br.com.tax_calculator_API.controlles.UserController;
import br.com.tax_calculator_API.dtos.JwtResponseDTO;
import br.com.tax_calculator_API.dtos.UserLoginDTO;
import br.com.tax_calculator_API.dtos.UserRequestDTO;
import br.com.tax_calculator_API.exeptions.UserAlreadyExistsException;
import br.com.tax_calculator_API.services.AuthService;
import br.com.tax_calculator_API.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    private UserRequestDTO userRequestDTO;
    private UserLoginDTO userLoginDTO;
    private JwtResponseDTO jwtResponseDTO;

    @BeforeEach
    void setUp() {
        userLoginDTO = new UserLoginDTO();
        jwtResponseDTO = new JwtResponseDTO("fake-jwt-token");
    }

    @Test
    void testRegisterUser_Success() {
        doNothing().when(userServiceImpl).registerUser(userRequestDTO);

        ResponseEntity<String> response = userController.registerUser(userRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User successfully registered.", response.getBody());
        verify(userServiceImpl, times(1)).registerUser(userRequestDTO);
    }

    @Test
    void testRegisterUser_UserAlreadyExistsException() {
        doThrow(new UserAlreadyExistsException("User already registered.")).when(userServiceImpl).registerUser(userRequestDTO);

        ResponseEntity<String> response = userController.registerUser(userRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already registered.", response.getBody());
    }

    @Test
    void testRegisterUser_InternalServerError() {
        doThrow(new RuntimeException("Unexpected error")).when(userServiceImpl).registerUser(userRequestDTO);

        ResponseEntity<String> response = userController.registerUser(userRequestDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error registering user.", response.getBody());
    }

    @Test
    void testLogin_Success() {
        when(authService.login(userLoginDTO)).thenReturn(jwtResponseDTO);

        ResponseEntity<JwtResponseDTO> response = userController.login(userLoginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtResponseDTO, response.getBody());
        verify(authService, times(1)).login(userLoginDTO);
    }
}
