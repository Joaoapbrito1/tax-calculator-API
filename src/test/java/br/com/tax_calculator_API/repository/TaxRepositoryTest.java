package br.com.tax_calculator_API.repository;

import br.com.tax_calculator_API.dtos.TaxResponseDTO;
import br.com.tax_calculator_API.models.TaxModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxRepositoryTest {

    @Mock
    private TaxRepository taxRepository;

    private TaxModel taxModel;

    @BeforeEach
    void setUp() {
        taxModel = new TaxModel();
        taxModel.setId(1L);
        taxModel.setName("Imposto Teste");
        taxModel.setDescription("Descrição do imposto teste");
        taxModel.setAliquot(10.0);
    }

    @Test
    void testFindByName_WhenTaxExists_ShouldReturnTaxResponseDTO() {
        when(taxRepository.findByName("Imposto Teste")).thenReturn(Optional.of(taxModel));

        Optional<TaxModel> foundTax = taxRepository.findByName("Imposto Teste");

        assertTrue(foundTax.isPresent());

        TaxResponseDTO taxResponseDTO = new TaxResponseDTO(
                foundTax.get().getId(),
                foundTax.get().getName(),
                foundTax.get().getDescription(),
                foundTax.get().getAliquot()
        );

        assertEquals("Imposto Teste", taxResponseDTO.getName());
        assertEquals("Descrição do imposto teste", taxResponseDTO.getDescription());
        assertEquals(10.0, taxResponseDTO.getAliquot());

        verify(taxRepository, times(1)).findByName("Imposto Teste");
    }

    @Test
    void testFindByName_WhenTaxDoesNotExist_ShouldReturnEmpty() {
        when(taxRepository.findByName("Imposto Inexistente")).thenReturn(Optional.empty());

        Optional<TaxModel> foundTax = taxRepository.findByName("Imposto Inexistente");

        assertFalse(foundTax.isPresent());

        verify(taxRepository, times(1)).findByName("Imposto Inexistente");
    }
}