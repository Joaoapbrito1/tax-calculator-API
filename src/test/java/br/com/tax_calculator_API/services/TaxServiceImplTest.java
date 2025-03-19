
package br.com.tax_calculator_API.services;

import br.com.tax_calculator_API.dtos.TaxCalculationRequestDTO;
import br.com.tax_calculator_API.dtos.TaxCalculationResponseDTO;
import br.com.tax_calculator_API.dtos.TaxRequestDTO;
import br.com.tax_calculator_API.dtos.TaxResponseDTO;
import br.com.tax_calculator_API.exeptions.TaxAlreadyExistsException;
import br.com.tax_calculator_API.exeptions.TaxInvalidDataException;
import br.com.tax_calculator_API.exeptions.TaxNotFoundException;
import br.com.tax_calculator_API.models.TaxModel;
import br.com.tax_calculator_API.repository.TaxRepository;
import br.com.tax_calculator_API.services.impl.TaxServiceImpl;
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
        taxModel.setName("Test Tax");
        taxModel.setDescription("Tax description");
        taxModel.setAliquot(10.0);

        taxRequestDTO = new TaxRequestDTO();
        taxRequestDTO.setName("Test Tax");
        taxRequestDTO.setDescription("Tax description");
        taxRequestDTO.setAliquot(10.0);
    }

    @Test
    void deleteTax_Success() {
        when(taxRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taxRepository).deleteById(1L);

        assertDoesNotThrow(() -> taxService.deleteTax(1L));

        verify(taxRepository, times(1)).existsById(1L);
        verify(taxRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTax_NotFound() {
        when(taxRepository.existsById(1L)).thenReturn(false);

        TaxNotFoundException exception = assertThrows(TaxNotFoundException.class, () -> taxService.deleteTax(1L));

        System.out.println("Mensagem de erro real: " + exception.getMessage());
        assertEquals("Tax not found with ID 1", exception.getMessage());
        verify(taxRepository, times(1)).existsById(1L);
        verify(taxRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateTax_Success() {
        when(taxRepository.findById(1L)).thenReturn(Optional.of(taxModel));
        when(taxRepository.save(any(TaxModel.class))).thenReturn(taxModel);

        TaxResponseDTO updatedTax = taxService.updateTax(1L, taxRequestDTO);

        assertNotNull(updatedTax);
        assertEquals("Test Tax", updatedTax.getName());
        verify(taxRepository, times(1)).findById(1L);
        verify(taxRepository, times(1)).save(any(TaxModel.class));
    }

    @Test
    void updateTax_NotFound() {
        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        TaxNotFoundException exception = assertThrows(TaxNotFoundException.class, () -> taxService.updateTax(1L, taxRequestDTO));

        assertEquals("Tax not found with ID: 1", exception.getMessage());
        verify(taxRepository, times(1)).findById(1L);
        verify(taxRepository, never()).save(any(TaxModel.class));
    }

    @Test
    void calculateTax_Success() {
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

        TaxCalculationResponseDTO response = taxService.calculateTax(requestDTO);

        assertEquals("Tax1", response.getTaxType());
        assertEquals(baseValue, response.getBaseValue());
        assertEquals(aliquot, response.getAliquot());
        assertEquals(expectedTaxValue, response.getValueTax());

        verify(taxRepository, times(1)).findById(taxId);
    }

    @Test
    void calculateTax_NotFound() {
        Long taxId = 1L;
        TaxCalculationRequestDTO requestDTO = new TaxCalculationRequestDTO(taxId, 100.0);

        when(taxRepository.findById(taxId)).thenReturn(Optional.empty());

        TaxNotFoundException exception = assertThrows(TaxNotFoundException.class, () -> taxService.calculateTax(requestDTO));

        System.out.println("Mensagem de erro real: " + exception.getMessage());
        assertEquals("Tax not found with ID 1", exception.getMessage());

        verify(taxRepository, times(1)).findById(taxId);
    }

    @Test
    void createTaxes_Success() {
        List<TaxRequestDTO> taxRequestDTOs = Arrays.asList(
                new TaxRequestDTO("Tax 1", "Description 1", 5.0),
                new TaxRequestDTO("Tax 2", "Description 2", 10.0)
        );

        when(taxRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(taxRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<TaxResponseDTO> response = taxService.createTaxes(taxRequestDTOs);

        assertNotNull(response);
        assertEquals(2, response.size());
        verify(taxRepository, times(2)).findByName(anyString());
        verify(taxRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createTaxes_Duplicate() {
        List<TaxRequestDTO> taxRequestDTOs = Arrays.asList(
                new TaxRequestDTO("Tax 1", "Description 1", 5.0)
        );

        when(taxRepository.findByName("Tax 1")).thenReturn(Optional.of(new TaxModel()));

        assertThrows(TaxAlreadyExistsException.class, () -> taxService.createTaxes(taxRequestDTOs));

        verify(taxRepository, times(1)).findByName("Tax 1");
        verify(taxRepository, never()).saveAll(anyList());
    }

    @Test
    void createTaxes_EmptyList() {
        List<TaxRequestDTO> emptyList = Arrays.asList();

        assertThrows(TaxInvalidDataException.class, () -> taxService.createTaxes(emptyList));

        verify(taxRepository, never()).findByName(anyString());
        verify(taxRepository, never()).saveAll(anyList());
    }

    @Test
    void mapToModel_Correct() {
        TaxRequestDTO dto = new TaxRequestDTO("Test Tax", "Tax description", 10.0);

        TaxModel taxModel = taxService.mapToTaxModel(dto);

        assertNotNull(taxModel);
        assertEquals("Test Tax", taxModel.getName());
        assertEquals("Tax description", taxModel.getDescription());
        assertEquals(10.0, taxModel.getAliquot());
    }

    @Test
    void allTaxes_ReturnAll() {
        List<TaxModel> taxModels = Arrays.asList(
                new TaxModel(1L, "Tax 1", "Description 1", 5.0),
                new TaxModel(2L, "Tax 2", "Description 2", 10.0)
        );

        when(taxRepository.findAll()).thenReturn(taxModels);

        List<TaxResponseDTO> response = taxService.allTaxes();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Tax 1", response.get(0).getName());
        assertEquals("Tax 2", response.get(1).getName());

        verify(taxRepository, times(1)).findAll();
    }
    @Test
    void shouldFindTaxById() {
        when(taxRepository.findById(1L)).thenReturn(Optional.of(taxModel));

        Optional<TaxModel> foundTax = taxService.searchById(1L);

        assertTrue(foundTax.isPresent());
        assertEquals(1L, foundTax.get().getId());
        assertEquals("Test Tax", foundTax.get().getName());

        verify(taxRepository, times(1)).findById(1L);
    }
    @Test
    void taxId_NotFound() {
        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TaxModel> foundTax = taxService.searchById(1L);

        assertFalse(foundTax.isPresent());
        verify(taxRepository, times(1)).findById(1L);
    }
}
