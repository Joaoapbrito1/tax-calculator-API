package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private UserModel userModel;

    @BeforeEach
    void setUp() {
        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setUsername("usuarioTeste");
        userModel.setPassword("senha123");
    }

    @Test
    void testFindByUsername_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByUsername("usuarioTeste")).thenReturn(Optional.of(userModel));

        Optional<UserModel> foundUser = userRepository.findByUsername("usuarioTeste");

        assertTrue(foundUser.isPresent());
        assertEquals("usuarioTeste", foundUser.get().getUsername());

        verify(userRepository, times(1)).findByUsername("usuarioTeste");
    }

    @Test
    void testFindByUsername_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findByUsername("naoExiste")).thenReturn(Optional.empty());

        Optional<UserModel> foundUser = userRepository.findByUsername("naoExiste");

        assertFalse(foundUser.isPresent());

        verify(userRepository, times(1)).findByUsername("naoExiste");
    }

    @Test
    void testExistsByUsername_WhenUserExists_ShouldReturnTrue() {
        when(userRepository.existsByUsername("usuarioTeste")).thenReturn(true);

        boolean exists = userRepository.existsByUsername("usuarioTeste");

        assertTrue(exists);

        verify(userRepository, times(1)).existsByUsername("usuarioTeste");
    }

    @Test
    void testExistsByUsername_WhenUserDoesNotExist_ShouldReturnFalse() {
        when(userRepository.existsByUsername("naoExiste")).thenReturn(false);

        boolean exists = userRepository.existsByUsername("naoExiste");

        assertFalse(exists);

        verify(userRepository, times(1)).existsByUsername("naoExiste");
    }
}
