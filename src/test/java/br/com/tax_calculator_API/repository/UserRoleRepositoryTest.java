package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.models.UserRoleModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleRepositoryTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserRoleModel userRoleModel;

    @BeforeEach
    void setUp() {
        userRoleModel = new UserRoleModel();
        userRoleModel.setId(1L);
        userRoleModel.setName("ROLE_ADMIN");
    }

    @Test
    void testSaveUserRole_ShouldReturnSavedRole() {
        when(userRoleRepository.save(userRoleModel)).thenReturn(userRoleModel);

        UserRoleModel savedRole = userRoleRepository.save(userRoleModel);

        assertNotNull(savedRole);
        assertEquals("ROLE_ADMIN", savedRole.getName());

        verify(userRoleRepository, times(1)).save(userRoleModel);
    }

    @Test
    void testFindById_WhenRoleExists_ShouldReturnRole() {
        when(userRoleRepository.findById(1L)).thenReturn(Optional.of(userRoleModel));

        Optional<UserRoleModel> foundRole = userRoleRepository.findById(1L);

        assertTrue(foundRole.isPresent());
        assertEquals("ROLE_ADMIN", foundRole.get().getName());

        verify(userRoleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_WhenRoleDoesNotExist_ShouldReturnEmpty() {
        when(userRoleRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<UserRoleModel> foundRole = userRoleRepository.findById(2L);

        assertFalse(foundRole.isPresent());

        verify(userRoleRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteUserRole_ShouldRemoveRole() {
        doNothing().when(userRoleRepository).deleteById(1L);

        userRoleRepository.deleteById(1L);

        verify(userRoleRepository, times(1)).deleteById(1L);
    }
}