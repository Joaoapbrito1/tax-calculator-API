package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.TaxCalculationRequestDTO;
import br.com.tax_calculator_API.dtos.TaxCalculationResponseDTO;
import br.com.tax_calculator_API.dtos.TaxRequestDTO;
import br.com.tax_calculator_API.dtos.TaxResponseDTO;
import br.com.tax_calculator_API.models.TaxModel;
import br.com.tax_calculator_API.repository.TaxRepository;
import br.com.tax_calculator_API.services.impl.TaxServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxServiceImplTest {

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private TaxServiceImpl taxService;

    private TaxModel taxModel;
    private TaxRequestDTO taxRequestDTO;

    @BeforeEach
    void setUp() {
        taxModel = new TaxModel();
        taxModel.setId(1L);
        taxModel.setName("Imposto Teste");
        taxModel.setDescription("Descrição do imposto");
        taxModel.setAliquot(10.0);

        taxRequestDTO = new TaxRequestDTO();
        taxRequestDTO.setName("Imposto Teste");
        taxRequestDTO.setDescription("Descrição do imposto");
        taxRequestDTO.setAliquot(10.0);
    }

    @Test
    void shouldCreateTaxesSuccessfully() {
        List<TaxRequestDTO> taxRequestDTOList = Arrays.asList(taxRequestDTO, taxRequestDTO);
        List<TaxModel> taxModelList = Arrays.asList(taxModel, taxModel);

        when(taxRepository.saveAll(anyList())).thenReturn(taxModelList);

        List<TaxResponseDTO> responseDTOList = taxService.createTaxes(taxRequestDTOList);

        assertNotNull(responseDTOList);
        assertEquals(2, responseDTOList.size());
        verify(taxRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldGetAllTaxesSuccessfully() {
        when(taxRepository.findAll()).thenReturn(List.of(taxModel));

        List<TaxResponseDTO> responseDTOList = taxService.allTaxes();

        assertNotNull(responseDTOList);
        assertEquals(1, responseDTOList.size());
        verify(taxRepository, times(1)).findAll();
    }

    @Test
    void shouldFindTaxById() {
        when(taxRepository.findById(1L)).thenReturn(Optional.of(taxModel));

        Optional<TaxModel> foundTax = taxService.searchById(1L);

        assertTrue(foundTax.isPresent());
        assertEquals(1L, foundTax.get().getId());
        verify(taxRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaxNotFoundById() {
        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TaxModel> foundTax = taxService.searchById(1L);

        assertFalse(foundTax.isPresent());
        verify(taxRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteTaxSuccessfully() {
        when(taxRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taxRepository).deleteById(1L);

        assertDoesNotThrow(() -> taxService.deleteTax(1L));

        verify(taxRepository, times(1)).existsById(1L);
        verify(taxRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTax() {
        when(taxRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> taxService.deleteTax(1L));

        assertEquals("Tax not found with id: 1", exception.getMessage());
        verify(taxRepository, times(1)).existsById(1L);
        verify(taxRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldUpdateTaxSuccessfully() {
        when(taxRepository.findById(1L)).thenReturn(Optional.of(taxModel));
        when(taxRepository.save(any(TaxModel.class))).thenReturn(taxModel);

        TaxResponseDTO updatedTax = taxService.updateTax(1L, taxRequestDTO);

        assertNotNull(updatedTax);
        assertEquals("Imposto Teste", updatedTax.getName());
        verify(taxRepository, times(1)).findById(1L);
        verify(taxRepository, times(1)).save(any(TaxModel.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTax() {
        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taxService.updateTax(1L, taxRequestDTO));

        assertEquals("Tax not found with id: 1", exception.getMessage());
        verify(taxRepository, times(1)).findById(1L);
        verify(taxRepository, never()).save(any(TaxModel.class));
    }
    @Test
    void testCalculateTax_Success() {
        // Arrange
        Long taxId = 1L;
        Double baseValue = 100.0;
        Double aliquot = 10.0;
        Double expectedTaxValue = 10.0;

        TaxModel taxModel = new TaxModel();
        taxModel.setId(taxId);
        taxModel.setName("Tax1");
        taxModel.setDescription("Description1");
        taxModel.setAliquot(aliquot);

        TaxCalculationRequestDTO requestDTO = new TaxCalculationRequestDTO(taxId, baseValue);

        when(taxRepository.findById(taxId)).thenReturn(Optional.of(taxModel));

        // Act
        TaxCalculationResponseDTO response = taxService.calculateTax(requestDTO);

        // Assert
        assertEquals("Tax1", response.getTaxType());
        assertEquals(baseValue, response.getBaseValue());
        assertEquals(aliquot, response.getAliquot());
        assertEquals(expectedTaxValue, response.getValueTax());

        verify(taxRepository, times(1)).findById(taxId);
    }

    @Test
    void testCalculateTax_TaxNotFound() {
        // Arrange
        Long taxId = 1L;
        TaxCalculationRequestDTO requestDTO = new TaxCalculationRequestDTO(taxId, 100.0);

        when(taxRepository.findById(taxId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> taxService.calculateTax(requestDTO));
        assertEquals("Imposto não encontrado com ID: " + taxId, exception.getMessage());

        verify(taxRepository, times(1)).findById(taxId);
    }
}
